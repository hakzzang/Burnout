package com.hbs.burnout.ui.main.adapter

import com.hbs.burnout.R
import com.hbs.burnout.model.Stage
import com.hbs.burnout.model.StageProgress

object MissionHelper {
    const val MISSION_SIZE = 10
    private val badge = arrayListOf(
        R.drawable.watermelon, R.drawable.banana, R.drawable.mango,
        R.drawable.broccoli, R.drawable.apple, R.drawable.strawberry,
        R.drawable.tangerine, R.drawable.paprika, R.drawable.orange,
        R.drawable.tomato
    )

    private val stages = listOf(
        Stage(1, "Mission1", "게임을 시작해보기", "", StageProgress.NOT_COMPLETED),
        Stage(2, "Mission2", "때로는 귀찮으니까 새를 그려보면 좋을지도?", "", StageProgress.NOT_COMPLETED),
        Stage(3, "Mission3", "집 주변에 새가 있을지도?", "", StageProgress.NOT_COMPLETED),
        Stage(4, "Mission4", "아~ 귀찮당... 밖에 나가기 싫은데...", "", StageProgress.NOT_COMPLETED),
        Stage(5, "Mission5", "블라블라블라 블라블라블라", "", StageProgress.NOT_COMPLETED),
        Stage(6, "Mission6", "블라블라블라 블라블라블라", "", StageProgress.NOT_COMPLETED),
        Stage(7, "Mission7", "블라블라블라 블라블라블라", "", StageProgress.NOT_COMPLETED),
        Stage(8, "Mission8", "블라블라블라 블라블라블라", "", StageProgress.NOT_COMPLETED),
        Stage(9, "Mission9", "블라블라블라 블라블라블라", "", StageProgress.NOT_COMPLETED),
        Stage(10, "Final Mission", "이제는 마지막이야. 모든건 별게 아니지?", "", StageProgress.NOT_COMPLETED)
    )

    fun getBadge(stageRound: Int): Int {
        return badge[stageRound - 1]
    }

    fun clearStageList(completedStages: List<Stage>): MutableList<Stage> {
        val editableCompletedStages = completedStages.toMutableList()
        if(editableCompletedStages.isEmpty()){
            val mission = stages[0]
            mission.progress = StageProgress.PLAYING
            editableCompletedStages.add(mission)
        }
        else if (editableCompletedStages.isNotEmpty() && editableCompletedStages.last().progress == StageProgress.COMPLETED) {
            if (editableCompletedStages.size < MISSION_SIZE) {
                val mission = stages[editableCompletedStages.size]
                mission.progress = StageProgress.PLAYING
                editableCompletedStages.add(mission)
            }
        }
        stages.forEachIndexed { index, mission ->
            if (editableCompletedStages.lastIndex < index) {
                editableCompletedStages.add(mission)
            }
        }

        return editableCompletedStages
    }
}