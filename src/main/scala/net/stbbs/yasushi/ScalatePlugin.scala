package net.stbbs.yasushi

import sbt._
import FileUtilities._

trait ScalatePlugin extends DefaultWebProject {

  def templateRoots: PathFinder = mainResources
  def generatedDirectory = outputRootPath / "gen"
  override def mainSourceRoots = super.mainSourceRoots +++ (generatedDirectory##)
  override def watchPaths = super.watchPaths --- (generatedDirectory ***)

  override def compileAction = super.compileAction dependsOn(precompile)

  def useServletRenderContext = true

  private def precompileOptions =
    (if(useServletRenderContext) Seq("--servlet") else Seq.empty) ++
    Seq("-o", generatedDirectory.absolutePath) ++
    templateRoots.getPaths

  lazy val precompile = precompileAction
  def precompileAction = task {createDirectory(generatedDirectory, log)} && runTask(Some("net.stbbs.yasushi.ScalatePrecompiler"), info.pluginsManagedDependencyPath ** "*.jar", precompileOptions)

}
