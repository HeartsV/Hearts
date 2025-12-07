package de.htwg.se.Hearts.controller

import de.htwg.se.Hearts.model._

object ChainOfResponsibility:

    final case class MoveContext(
        game: Game,
        playerHand: List[Card],
        index: Int
    )

    type CardRule = MoveContext => Either[String, Unit]

    val indexMustBeInBounds: CardRule = ctx =>
        if ctx.index < 0 || ctx.index >= ctx.playerHand.size then
            Left(s"Index: ${ctx.index} was out of bounds!\n")
        else
            Right(())

    val firstTrickMustStartWithTwoOfClubs: CardRule = ctx =>
        if ctx.game.firstCard then
            val card = ctx.playerHand(ctx.index)
            if card == Card(Rank.Two, Suit.Clubs) then
                Right(())
            else
                Left("First trick must start with 2 \u2663!\n")
        else
            Right(())

    val mustFollowSuitIfPossible: CardRule = ctx =>
        ctx.game.trickCards.headOption match
            case None => Right(())
            case Some(leadCard) =>
                val hasSuit = ctx.playerHand.exists(_.suit == leadCard.suit)
                val playedCard = ctx.playerHand(ctx.index)

                if hasSuit && playedCard.suit != leadCard.suit then
                    Left(f"You have at least one card with Suit ${leadCard.suit}! You must follow this Suit!\n")
                else
                    Right(())

    val heartsOnlyIfBrokenOrNoAlternative: CardRule = ctx =>
        val card = ctx.playerHand(ctx.index)

        if card.suit != Suit.Hearts then
            Right(())
        else if ctx.game.startWithHearts || ctx.playerHand.forall(_.suit == Suit.Hearts) then
            Right(())
        else
            Left("You cannot play \u2665 until they are broken or you have only \u2665s\n")

    val validationChain: List[CardRule] =
        List(
            indexMustBeInBounds,
            firstTrickMustStartWithTwoOfClubs,
            mustFollowSuitIfPossible,
            heartsOnlyIfBrokenOrNoAlternative
        )

    def validateMove(game: Game, playerHand: List[Card], index: Int): Either[String, Card] =
        val ctx = MoveContext(game, playerHand, index)

        val result: Either[String, Unit] =
            validationChain.foldLeft[Either[String, Unit]](Right(())) { (previousResult, rule) =>
                previousResult.flatMap(_ => rule(ctx))
            }
        result.map(_ => ctx.playerHand(index))



