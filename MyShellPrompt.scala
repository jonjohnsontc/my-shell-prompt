//> using scala 3.7.1
//> using platform native
//> using nativeVersion 0.5.8

//> using nativeMode release-fast

//> using toolkit default

import scala.io.AnsiColor._

object Prompt:
  
  // Hello! I display the following in separate lines:
  // Current python3 env 
  // Current Java env
  // Current Git branch
  // file-path to root of git directory
  // User @ Host in current working directory
  // Final prompt char
  @main def display(): Unit = 
    given currentDir: WorkingDir = WorkingDir(os.pwd)
    var localDir = String()
    if currentDir.path.lastOpt.isDefined then
      localDir = currentDir.path.last
    else
      localDir = currentDir.path.toString
    val host = os.proc("hostname").call().out.trim().toLowerCase
    
    val prompt = List(
      PythonEnv.display.map(env =>s"${GREEN}${BOLD}Py env: ${env}${RESET}"),
      GitBranch.display.map(br => s"${BLUE}${BOLD}On branch ${br} ${RESET}"),
      LongPath.display.map(lp => s"${RED}${BOLD}${lp}"),
      Some(s" ❧ JJ @ ${host} IN ${localDir} ☙ ${RESET}"),
      Some(s"zsh>> ")
    ).flatten.mkString("\n")
    println(prompt)
  
  // Represents the current working directory
  // Can be passed into all of the Prompt Row objects below by the compiler
  // TODO: what value does declaring an arg a 'val'
  class WorkingDir(val path: os.Path)

  // Single line of a prompt 
  trait Row:
    def display(using dir: WorkingDir): Option[String]

  object GitBranch extends Row:
    def display(using dir: WorkingDir) = 
      findGetRoot(dir.path).map { root =>
        val head = os.read(root / "HEAD").trim
        if head.startsWith("ref: refs/heads/") then
          head.substring("ref: refs/heads/".length)
        else
          head.take(7)  // Detached HEAD SHA
      }

    def findGetRoot(path: os.Path = os.pwd): Option[os.Path] =
      if os.exists(path / ".git") then 
        Some(path / ".git")
      else 
        if path.toString != path.root then
          findGetRoot(path / os.up)
        else
          None

  object PythonEnv extends Row:
    inline def display(using dir: WorkingDir) = 
      val vars = System.getenv()
      val sep = "/"
      val vEnv = vars.get("VIRTUAL_ENV")
      if vEnv != null then
        Some(vEnv.split(sep).last)
      else
        None 
  
  object LongPath extends Row:
    inline def display(using dir: WorkingDir) = 
      val home = System.getenv("HOME")
      val pwd = dir.path.toString
      val homeChar = "~"

      if pwd.startsWith(home) then
        Some(s"${pwd.replace(home, homeChar)}")
      else
        Some(pwd)

  object AWSEnv extends Row:
    inline def display(using dir: WorkingDir) = ???

