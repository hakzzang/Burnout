package com.hbs.burnout.utils.script

import com.hbs.burnout.model.Script
import com.hbs.burnout.model.ScriptBuilder
import javax.inject.Inject

class ScriptStorage @Inject constructor() {
    private val mission1 = listOf(
        Script(0, "안녕하세요. 저는 번아웃 게임을 이끌어가는 새우버거라고 합니다.", 0, 1, 0),
        ScriptBuilder(1, "응...그래;;", 1, 1, 1).addAnswer(
            mapOf(
                0 to "응...그래;;",
                1 to "뭔데 나한테 인사해?",
                2 to "응~~ 귀찮아~~ 맘대로해~~"
            )
        ).create(),
        ScriptBuilder(0, "처음엔 어색할 수 있겠지만, 게임은 당신과 제가 대화를 통해서 이루어질겁니다. ", 2, 1, 2).addAnswer(
            mapOf(
                0 to "처음엔 어색할 수 있겠지만, 게임은 당신과 제가 대화를 통해서 이루어질겁니다.",
                1 to "하하... 무례하시군요. 여튼, 게임은 당신과 제가 대화를 통해서 이루어질겁니다.",
                2 to "응~~ 그러면 설명부터 들어봐~~ 게임은 너와 내가 대화를 통해서 진행할거야~"
            )
        ).create(),
        ScriptBuilder(1, "그래. 알겠어. 좋아 어떤걸 하면 될까?", 1, 1, 3).addAnswer(
            mapOf(
                0 to "그래. 알겠어. 좋아 어떤걸 하면 될까?",
                1 to "그렇단 말이지..",
                2 to "ㅇㅋㅇㅋ 다음은?"
            )
        ).create(),
        Script(0, "게임은 10개의 미션으로 진행됩니다.", 0, 1, 4),
        Script(0, "벌써 1번째 미션은 끝나가고 있습니다.", 0, 1, 5),
        Script(0, "10 9 8 7 6 5 4 3 2 1", 0, 1, 6),
        Script(0, "축하합니다. 1번째 미션을 클리어했습니다.", 0, 1, 7),
        Script(0, "2번째 미션은 어떤게 있는지 궁금하다면, 다음 미션으로 진행해볼까?", 0, 1, 8)
    )

    private val mission2 = listOf(
        Script(0, "2번째 미션에 도착했어.", 0, 2, 0),
        ScriptBuilder(1, "", 1, 2, 1).addAnswer(
            mapOf(
                0 to "좋아. 뭐지?",
                1 to "..."
            )
        ).create(),
        ScriptBuilder(0, "2번째 미션은 집 근처의 새를 찾는거야.", 0, 2, 2).create(),
        ScriptBuilder(0, "가끔은 집 근처의 새에 관심을 갖는 것도 의미있지 않을까?", 0, 2, 3).create(),
        ScriptBuilder(1, "", 1, 2, 4).addAnswer(
            mapOf(
                0 to "좋아. 새를 찾으면 뭐를 하면 되지?",
                1 to "흠~~ 귀찮은데~~"
            )
        ).create(),
        ScriptBuilder(0, "새를 찾으면, 카메라로 사진을 찍을거야.", 2, 2, 5).addAnswer(
            mapOf(
                0 to "새를 찾으면, 카메라로 사진을 찍을거야.",
                1 to "움직이다보면 귀찮은 것도 금방 사라질거야."
            )
        ).create(),
        Script(0, "새를 찾으면 나에게 말을 걸어줘", 0, 2, 6),
        Script(1, "새를 찾았어!!", 3, 2, 7),
        Script(0, "봐봐. 해낼 줄 알았어.", 0, 2, 8),
        Script(0, "축하해. 벌써 2번째 미션이 종료되었어.", 0, 2, 9),
        Script(0, "너에게 또 뱃지를 줄게.", 0, 2, 10)
    )

    fun search(scriptNumber: Int): List<Script> {
        when (scriptNumber) {
            1 -> return mission1
            2 -> return mission2
        }

        return mission1
    }
}
