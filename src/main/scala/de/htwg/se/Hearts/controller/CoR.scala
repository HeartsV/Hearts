//CoR steht für ChainOfResponsibility
package de.htwg.se.Hearts.controller

import de.htwg.se.Hearts.model._

object ChainOfResponsibility {

    final case class MoveContext(
        game: Game,
        player: Player,
        card: Card
    )

    type CardRule = MoveContext => Boolean

    val mustHaveCard: CardRule = ctx => ctx.player.hand.contains(ctx.card)

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

    def validateMove(game: Game, player: Player, card: Card): Boolean =
        val ctx = MoveContext(game, player, card)
        validationChain.forall(rule => rule(ctx))
}


