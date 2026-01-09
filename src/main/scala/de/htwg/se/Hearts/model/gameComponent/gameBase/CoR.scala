package de.htwg.se.Hearts.model.gameComponent.gameBase

import de.htwg.se.Hearts.model.gameComponent.GameInterface
import de.htwg.se.Hearts.model.gameComponent.CoRInterface
import de.htwg.se.Hearts.model.gameComponent.Suit
import de.htwg.se.Hearts.model.gameComponent.Rank

class ChainOfResponsibility extends CoRInterface:

    final case class MoveContext(
        game: GameInterface,
        playerHand: List[Card],
        index: Option[Int]
    )

    type CardRule = MoveContext => Either[String, Unit]

    val indexNotNone: CardRule = ctx =>
        if ctx.index == None then
            Left(s"Index was not a number!\n")
        else
            Right(())

    val indexMustBeInBounds: CardRule = ctx =>
        if ctx.index.get < 1 || ctx.index.get>= ctx.playerHand.size + 1 then
            Left(s"Index: ${ctx.index.get} was out of bounds!\n")
        else
            Right(())

    val firstTrickMustStartWithTwoOfClubs: CardRule = ctx =>
        if ctx.game.getFirstCard then
            val card = ctx.playerHand(ctx.index.get - 1)
            if card == Card(Rank.Two, Suit.Clubs) then
                Right(())
            else
                Left("First trick must start with 2 \u2663!\n")
        else
            Right(())

    val mustFollowSuitIfPossible: CardRule = ctx =>
        ctx.game.getTrickCards.headOption match
            case None => Right(())
            case Some(leadCard) =>
                val hasSuit = ctx.playerHand.exists(_.suit == leadCard.suit)
                val playedCard = ctx.playerHand(ctx.index.get - 1 )

                if hasSuit && playedCard.suit != leadCard.suit then
                    Left(f"You have at least one card with Suit ${leadCard.suit}! You must follow this Suit!\n")
                else
                    Right(())

    val heartsOnlyIfBrokenOrNoAlternative: CardRule = ctx =>
        val card = ctx.playerHand(ctx.index.get - 1)

        if card.getSuit != Suit.Hearts then
            Right(())
        else if ctx.game.getStartWithHearts || ctx.playerHand.forall(_.suit == Suit.Hearts) || ctx.game.getTrickCards.nonEmpty then
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

    def validateMove(game: GameInterface, playerHand: List[Card], index: Option[Int]): Either[String, Card] =
        val ctx = MoveContext(game, playerHand, index)

        val result: Either[String, Unit] =
            validationChain.foldLeft[Either[String, Unit]](Right(())) { (previousResult, rule) =>
                previousResult.flatMap(_ => rule(ctx))
            }
        result.map(_ => ctx.playerHand(index.get - 1))



