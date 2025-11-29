package de.htwg.se.Hearts.model

enum Suit extends Ordered[Suit]:

    case Hearts, Spades, Diamonds, Clubs

    def fileName: String = this match
        case Hearts     => "hearts"
        case Spades     => "spades"
        case Diamonds   => "diamonds"
        case Clubs      => "clubs"

    def compare(that: Suit): Int =
        this.ordinal.compare(that.asInstanceOf[Suit].ordinal)

    override def toString: String = this match
        case Hearts     => "\u2665"
        case Spades     => "\u2660"
        case Diamonds   => "\u2666"
        case Clubs      => "\u2663"