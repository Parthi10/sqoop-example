package com.datamountaineer.ingestor.sqoop

import com.cloudera.sqoop.SqoopOptions
import com.cloudera.sqoop.SqoopOptions.IncrementalMode
import com.datamountaineer.ingestor.utils.Constants
import com.datamountaineer.ingestor.{IngestorTest, IngestorTestTrait}
import org.scalatest._
import org.scalatest.mock.MockitoSugar

import scala.collection.JavaConversions._

class IngestSqoopTest extends IngestorTestTrait with BeforeAndAfter with MockitoSugar {
  var INGEST: IngestSqoop = _
  var OPTIONS: SqoopOptions = _

  before {
    INGEST = new IngestSqoop(IngestorTest.INPUT, true)
    OPTIONS = INGEST.build_sqoop_options()
  }

  "InjestSqoop" should  {
    "create a SqoopOptions from input %s".format(IngestorTest.INPUT) in {
      val map = mapAsJavaMap(INGEST.params)
      map should have size 8
      map should contain(Entry(Constants.DB_TYPE_KEY, IngestorTest.MY_SQL))
      map should contain(Entry(Constants.SERVER_KEY, IngestorTest.LOCALHOST))
      map should contain(Entry(Constants.DATABASE_KEY, IngestorTest.TEST_DB))
      map should contain(Entry(Constants.TABLE_KEY, IngestorTest.TEST_TABLE))
      map should contain(Entry(Constants.SPLIT_BY_KEY, IngestorTest.SPLIT_BY_COL))
      map should contain(Entry(Constants.MAPPERS_KEY, IngestorTest.NUM_MAPPERS))
      map should contain(Entry(Constants.SPLIT_BY_KEY, IngestorTest.SPLIT_BY_COL))
      map should contain(Entry(Constants.LAST_VAL_KEY, "0"))
      OPTIONS shouldBe a[SqoopOptions]
    }

    "have connection string set" in {
      assert(OPTIONS.getConnectString === IngestorTest.CONNECTION_STRING)
    }
    "have table name set" in {
      assert(OPTIONS.getTableName === IngestorTest.TEST_TABLE)
    }
    "have direct mode set" in {
      assert(OPTIONS.isDirect, true)
    }
    "be incremental" in {
      assert(OPTIONS.getIncrementalMode === IncrementalMode.AppendRows)
    }
    "have check col set to %s".format(IngestorTest.SPLIT_BY_COL) in {
      assert(OPTIONS.getIncrementalTestColumn === IngestorTest.SPLIT_BY_COL)
    }
    "have Hive delimiters unset" in {
      assert(OPTIONS.getHiveDelimsReplacement === null)
    }
    "have password file should be set" in {
      assert(OPTIONS.getPasswordFilePath != null)
    }
    "have target_dir set to %s".format(IngestorTest.TARGET_DIR) in {
      assert(OPTIONS.getTargetDir === IngestorTest.TARGET_DIR)
    }
  }
}