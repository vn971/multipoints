package ru.narod.vn91.pointsop.server.irc

import org.scalatest._

class IrcNicknameManagerTest extends FunSuite with DiagrammedAssertions {
	test("nicknames") {
		val nicks = new IrcNicknameManager
		assert(nicks.fromIrc.size() == 0)
		assert(nicks.fromId.size() == 0)

		intercept[Exception](nicks.changeIrcNick("a", "b"))
		assert(nicks.id2irc("non-existent") == "")
		assert(nicks.fromIrc.size() == 0)
		assert(nicks.fromId.size() == 0)

		val firstIrc = "inDI_X220111100000[away]" // away

		val firstId = nicks.irc2id(firstIrc)
		assert(firstId == "inDI")
		assert(nicks.fromIrc.size() == 1)
		assert(nicks.fromId.size() == 1)

		val renamedIrc = "inDI_X220111123511[g101]" // in game
		nicks.changeIrcNick(firstIrc, renamedIrc)
		assert(nicks.irc2id(renamedIrc) == firstId)
		assert(nicks.id2irc(firstId) == renamedIrc)
		assert(nicks.fromIrc.size() == 1)
		assert(nicks.fromId.size() == 1)

		val secondIrc = "inDI_X220111100000[free]" // free
		val secondId = nicks.irc2id(secondIrc)
		assert(secondId != firstId)
		assert(secondId != secondIrc)
		assert(nicks.fromIrc.size() == 2)
		assert(nicks.fromId.size() == 2)

		nicks.removeIrcNick(renamedIrc)
		assert(nicks.id2irc(firstId) == "")
		assert(nicks.fromIrc.size() == 1)
		assert(nicks.fromId.size() == 1)

		val rejoinedId = nicks.irc2id(firstIrc)
		assert(rejoinedId == firstId)
		assert(nicks.fromIrc.size() == 2)
		assert(nicks.fromId.size() == 2)
	}
}
