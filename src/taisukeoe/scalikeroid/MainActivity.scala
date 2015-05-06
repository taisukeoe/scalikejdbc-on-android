package taisukeoe.scalikeroid

import java.sql.Timestamp

import _root_.android.app.Activity
import _root_.android.os.Bundle
import android.database.DatabaseUtils
import android.util.Log
import scalikejdbc._

class MainActivity extends Activity with TypedActivity {
  override def onCreate(bundle: Bundle) {
    super.onCreate(bundle)
    setContentView(R.layout.main)

    val fileName = "test.db"
    val dbPath = getDatabasePath(fileName)
    val dbVersion = 1
    DatabaseUtils.createDbFromSqlStatements(this,fileName,dbVersion,"")

    if(BuildConfig.DEBUG)Log.d("ScalikeJDBC on Android",s"DBPath ${dbPath}")

    Class.forName("org.sqldroid.SQLDroidDriver")

    ConnectionPool.singleton("jdbc:sqldroid:/" + dbPath,  null, null)

    implicit val session = AutoSession

    // table creation, you can run DDL by using #execute as same as JDBC
sql"""
  create table if not exists members(
  id INTEGER primary key autoincrement,
  name VARCHAR(64),
  created_at INTEGER not null
  )
""".execute.apply()

    // insert initial data
    Seq("Alice", "Bob", "Chris") foreach { name =>
      sql"insert into members (name, created_at) values (${name}, ${System.currentTimeMillis})".update.apply()
    }

    // find all members
    val members: List[Member] = sql"select * from members".map(rs => Member(rs)).list.apply()

    findView(TR.textview).setText(members.mkString(","))
  }
}

case class Member(id:Long ,name: Option[String], createdAt:Timestamp)

object Member extends SQLSyntaxSupport[Member] {
  override val tableName = "members"

  def apply(rs: WrappedResultSet) = new Member(
    rs.long("id"), rs.stringOpt("name"), rs.timestamp("created_at"))
}