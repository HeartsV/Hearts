package de.htwg.se.Hearts.controller

//import de.htwg.se.Hearts.model._
/*
object ChainOfResponsibility:

    final case class MoveContext(
        game: Game,
        playerHand: List[Card],
        index: Int
    )

    type CardRule = MoveContext => Either[String, Unit]

    //val mustHaveCard: CardRule = ctx => ctx.playerHand.contains(ctx.card)

    val indexMustBeInBounds: CardRule = ctx =>
        if (ctx.index < 0 || ctx.index >= ctx.playerHand.size)
            Left(f"Index: ${ctx.index} was out of bounds!")
        else
            Right(())

    val firstTrickMustStartWithTwoOfClubs: CardRule = ctx =>
        if ctx.game.firstCard then
            ctx.card == Card(Rank.Two, Suit.Clubs)
        else
            true

    val mustFollowSuitIfPossible: CardRule = ctx =>
        ctx.game.trickCards.headOption match
            case None =>
                true
            case Some(leadCard) =>
                val hasSuit = ctx.player.hand.exists(_.suit == leadCard.suit)
                if hasSuit then
                    ctx.card.suit == leadCard.suit
                else
                    true

    val heartsOnlyIfBrokenOrNoAlternative: CardRule = ctx =>
        if ctx.card.suit != Suit.Hearts then
            true
        else
            ctx.game.startWithHearts || ctx.player.hand.forall(_.suit == Suit.Hearts)

    val validationChain: List[CardRule] = List(mustHaveCard, firstTrickMustStartWithTwoOfClubs, mustFollowSuitIfPossible, heartsOnlyIfBrokenOrNoAlternative)

    def validateMove(game: Game, playerHand: List[Card], index: Int): Either[String, Unit] =
        val ctx = MoveContext(game, playerHand, index)
        validationChain.forall(rule => rule(ctx))*/

//package de.htwg.se.Hearts.controller

import de.htwg.se.Hearts.model._

object ChainOfResponsibility:

  final case class MoveContext(
      game: Game,
      playerHand: List[Card],
      index: Int
  )

  // Jede Regel: Entweder Fehlertext oder "alles okay"
  type CardRule = MoveContext => Either[String, Unit]

  // 1. Index muss im Bereich liegen
  val indexMustBeInBounds: CardRule = ctx =>
    if ctx.index < 0 || ctx.index >= ctx.playerHand.size then
      Left(s"Index: ${ctx.index} was out of bounds!")
    else
      Right(()) // alles okay

  // 2. Erste Stichkarte muss die 2♣ sein
  val firstTrickMustStartWithTwoOfClubs: CardRule = ctx =>
    if ctx.game.firstCard then
      val card = ctx.playerHand(ctx.index)
      if card == Card(Rank.Two, Suit.Clubs) then
        Right(())
      else
        Left("First trick must start with Two of Clubs")
    else
      Right(()) // Regel gilt nur für den allerersten Stich

  // 3. Farbe bedienen, wenn möglich
  val mustFollowSuitIfPossible: CardRule = ctx =>
    ctx.game.trickCards.headOption match
      case None =>
        // Du spielst als erster in diesem Stich -> keine Einschränkung
        Right(())
      case Some(leadCard) =>
        val hasSuit = ctx.playerHand.exists(_.suit == leadCard.suit)
        val playedCard = ctx.playerHand(ctx.index)

        if hasSuit && playedCard.suit != leadCard.suit then
          Left("You must follow suit if possible")
        else
          Right(())

  // 4. Herz nur, wenn gebrochen oder keine Alternative
  val heartsOnlyIfBrokenOrNoAlternative: CardRule = ctx =>
    val card = ctx.playerHand(ctx.index)

    if card.suit != Suit.Hearts then
      Right(()) // keine Herzkarte, Regel egal
    else if ctx.game.startWithHearts || ctx.playerHand.forall(_.suit == Suit.Hearts) then
      Right(()) // Herz ist erlaubt (gebrochen oder nur Herz auf der Hand)
    else
      Left("You cannot play hearts until they are broken or you have only hearts")

  // Die Kette der Regeln
  val validationChain: List[CardRule] =
    List(
      indexMustBeInBounds,
      firstTrickMustStartWithTwoOfClubs,
      mustFollowSuitIfPossible,
      heartsOnlyIfBrokenOrNoAlternative
    )

  // Hier jetzt Chain of Responsibility mit Either
  def validateMove(game: Game, playerHand: List[Card], index: Int): Either[String, Card] =
    val ctx = MoveContext(game, playerHand, index)

    // Wir gehen nacheinander durch alle Regeln
    val result: Either[String, Unit] =
      validationChain.foldLeft[Either[String, Unit]](Right(())) { (acc, rule) =>
        // Wenn schon ein Fehler (Left) aufgetreten ist, bleibt es Left
        // Wenn Right, dann nächste Regel ausführen
        acc.flatMap(_ => rule(ctx))
      }

    // Wenn alles ok (Right), gib die gespielte Karte zurück
    result.map(_ => ctx.playerHand(index))



