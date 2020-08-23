package com.hbs.burnout

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.hbs.burnout.model.Script
import com.hbs.burnout.model.dao.script.ScriptDao
import com.hbs.burnout.model.dao.script.StageDao
import com.hbs.burnout.model.dao.script.StageDataBase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import okio.IOException
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import javax.inject.Inject

@FixMethodOrder(MethodSorters.JVM)
@RunWith(AndroidJUnit4::class)
@MediumTest
@HiltAndroidTest
class SimpleChattingReadWriteTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var stageDataBase: StageDataBase

    private val scriptDao: ScriptDao by lazy {
        stageDataBase.getScriptDao()
    }
    private val stageDao: StageDao by lazy {
        stageDataBase.getStageDao()
    }

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun `1_IS_ABLE_TEST`() {
        Assert.assertEquals("True 케이스가 작동하는 지 확인하는 테스트", 1, 1)
    }

    @Test
    fun `2_IS_UNABLE_TEST`() {
        Assert.fail("False 케이스가 작동하는 지 확인하는 테스트")
        Assert.assertEquals(1, 0)
    }

    @Test
    fun `3_DROP_TABLE`() {
        runBlocking {
            scriptDao.dropTable()
            Assert.assertEquals("Script Table을 Drop하는 테스트", true, true)
        }
    }

    @Test
    fun `4_IS_COMPLETE_DROP_TABLE`() {
        runBlocking {
            val script = scriptDao.getAll()
            Assert.assertEquals("Drop 후, 비어있는 테이블을 확인하는 테스트",0, script.size)
        }
    }

    @Test
    fun `5_INSERT_SCRIPT`() {
        runBlocking {
            scriptDao.insert(Script(0, "안녕하세요", 0, 1, 0))
            Assert.assertEquals("Script Table에 아이템을 Insert하는 테스트",0, 0)
        }
    }

    @Test
    fun `6_HAS_ONE_SCRIPT`() {
        runBlocking {
            val script = scriptDao.getAll()
            assertEquals("Insert가 정상적으로 되어있는지 확인하는 테스트",1, script.size)
        }
    }

    @Test
    fun `99_DROP_TABLE`() {
        runBlocking {
            scriptDao.dropTable()
            assertEquals("Table을 Drop해서 초기화하는 테스트",1, 1)
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        stageDataBase.close()
    }

}