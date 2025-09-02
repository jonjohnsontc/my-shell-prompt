//> using scala 3.7.1
//> using platform native
//> using nativeVersion 0.5.8

//> using toolkit default

import scala.io.AnsiColor._

object Prompt:
  
  @main def display(): Unit = 
    val currentDir = os.pwd
    
    var localDir = String()
    if currentDir.lastOpt.isDefined then
      localDir = currentDir.last
    else
      localDir = currentDir.toString

    val host = os.proc("hostname").call().out.trim().toLowerCase
    
    val branch = getGitBranch() 
    if branch.isDefined then
      println(s"${BLUE}${BOLD} On branch ${branch.get} ${RESET}")

    println(s"${GREEN}${BOLD} ❧ JJ @ ${host} IN ${localDir} ☙ ${RESET}")
    println(s"zsh>> ")
  
  private def getGitBranch(path: os.Path = os.pwd): Option[String] = 
    if os.exists(path / ".git") then
      val head = os.read(os.pwd / ".git" / "HEAD").trim
      if head.startsWith("ref: refs/heads/") then
        Some(head.substring("ref: refs/heads".length))
      else
        Some(head.take(7))  // Detached HEAD SHA
    else
      None


