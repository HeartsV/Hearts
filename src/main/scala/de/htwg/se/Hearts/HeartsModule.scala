package de.htwg.se.Hearts

import com.google.inject.{AbstractModule,Provides}
import com.google.inject.name.Names
import net.codingwell.scalaguice.ScalaModule
import de.htwg.se.Hearts.model.gameComponent.GameInterface
import de.htwg.se.Hearts.model.gameComponent.gameBase.Game
import de.htwg.se.Hearts.controller.controllerComponent.ControllerInterface
import de.htwg.se.Hearts.controller.controllerComponent.controllerBase.Controller
import de.htwg.se.Hearts.model.gameComponent.DeckManagerInterface
import de.htwg.se.Hearts.model.gameComponent.gameBase.DeckManager
import de.htwg.se.Hearts.controller.scoringComponent.ScoringInterface
import de.htwg.se.Hearts.controller.scoringComponent.scoringBase.HeartsScoring
import de.htwg.se.Hearts.controller.playerTurnComponent.PlayerTurnInterface
import de.htwg.se.Hearts.controller.playerTurnComponent.playerTurnBase.PlayerTurn
import de.htwg.se.Hearts.controller.leaderBoardComponent.LeaderBoardInterface
import de.htwg.se.Hearts.controller.leaderBoardComponent.leaderBoardBase.LeaderBoard
import de.htwg.se.Hearts.model.gameComponent.BuilderInterface
import de.htwg.se.Hearts.model.gameComponent.gameBase.GameBuilder
import de.htwg.se.Hearts.model.gameComponent.DirectorInterface
import de.htwg.se.Hearts.model.gameComponent.gameBase.Director
import de.htwg.se.Hearts.model.gameComponent.CoRInterface
import de.htwg.se.Hearts.model.gameComponent.gameBase.ChainOfResponsibility
import de.htwg.se.Hearts.model.gameComponent.PlayerInterface
import de.htwg.se.Hearts.model.gameComponent.gameBase.Player

class HeartsModule extends AbstractModule with ScalaModule {

	override def configure(): Unit =
		bind(classOf[DeckManagerInterface]).to(classOf[DeckManager])
		bind(classOf[ScoringInterface]).to(classOf[HeartsScoring])
		bind(classOf[PlayerTurnInterface]).to(classOf[PlayerTurn])
		bind(classOf[LeaderBoardInterface]).to(classOf[LeaderBoard])
		bind(classOf[DirectorInterface]).to(classOf[Director])
		bind(classOf[CoRInterface]).to(classOf[ChainOfResponsibility])
		//bind(classOf[PlayerInterface]).to(classOf[Player])

		@Provides
		def providePlayer(): PlayerInterface =
			Player("", Nil, Nil, 0)

}
