package com.hbs.burnout.utils.script

import com.hbs.burnout.model.Script
import com.hbs.burnout.model.ScriptBuilder
import javax.inject.Inject

class ScriptStorage @Inject constructor(){
    private val mission1 = listOf(
        Script(0, "안녕하세요", 0, 1, 0 ),
        ScriptBuilder(1, "", 1, 1, 1).addAnswer(
            mapOf(
                0 to "응...그래;;",
                1 to "뭔데 나한테 인시해!?"
            )
        ).create(),
        ScriptBuilder(0, "저는 새우버거입니다.", 0, 1, 2).addAnswer(
            mapOf(
                0 to "하하^-^ 저는 새우버거라고 합니다.",
                1 to "ㅡㅡ 아주 무례하군요?"
            )
        ).create(),
        Script(0, "식사는 했어요", 0, 1, 3),
        Script(0, "^ㅠ^ 난 밥먹었지롱", 0, 1, 4),
        Script(0, "ㅡㅡ나갑니다", 0, 1, 5)
    )

    fun search(scriptNumber: Int): List<Script> {
        when (scriptNumber) {
            1 -> return mission1
        }

        return mission1
    }
}
