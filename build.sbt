val Scala3Version = "3.1.0"
val CatsEffectVersion = "3.3.4"
val CatsVersion = "2.7.0"
val CirceVersion = "0.14.1"
val Fs2Version = "3.2.4"
val Http4sVersion = "0.23.7"
val Http4sDomVersion = "0.2.0"
val DoobieVersion = "1.0.0-RC1"
val PureConfigVersion = "0.17.1"
val Log4CatsVersion = "2.1.1"
val LogBackVersion = "1.2.3"
val MunitVersion = "0.7.29"
val MunitCatsEffectVersion = "1.0.7"
val ScalaCheckEffectVersion = "1.0.3"
val TestContainersScalaVersion = "0.39.5"
val FlywayVersion = "7.10.0"
val SlinkyVersion = "0.7.0"
val ServerName = "scalaWebserver"
val ClientName = "scalaSlinkyClient"

lazy val commonSettings = Seq(
  version := "0.0.1",
  scalaVersion := Scala3Version,
  scalacOptions ++= Seq("-deprecation", "-feature", "-Ykind-projector"),
)

// reference https://github.com/shadaj/slinky/blob/main/build.sbt for inspiration!
// or https://github.com/ScalablyTyped/SlinkyDemos/blob/master/build.sbt
lazy val client =
  (project in file("client"))
    .settings(commonSettings)
    .settings(
      name := ClientName,
      scalaJSUseMainModuleInitializer := true,
      Compile / fastOptJS / scalaJSLinkerConfig ~= {
        _.withSourceMap(false)
      },
      scalaJSLinkerConfig := scalaJSLinkerConfig.value.withModuleKind(ModuleKind.CommonJSModule),
      Compile / npmDependencies ++= Seq(
        "react" -> "16.13.1",
        "react-dom" -> "16.13.1",
      ),
      /* Compile / npmDevDependencies ++= Seq(
        "webpack-merge" -> "4.2.2",
        "copy-webpack-plugin" -> "5.1.1",
        "html-webpack-plugin" -> "4.3.0"
      ), */
      webpackDevServerPort := 3004,
      addCommandAlias("fast", "fastOptJS::webpack"),
      addCommandAlias("dev", ";Compile / fastOptJS / startWebpackDevServer;~fastOptJS"),
      libraryDependencies ++= Seq(
        "org.typelevel" %% "cats-effect" % CatsEffectVersion,
        "org.http4s" %%% "http4s-dom" % Http4sDomVersion,
        "me.shadaj" %%% "slinky-core" % SlinkyVersion,
        "me.shadaj" %%% "slinky-web" % SlinkyVersion,
      )
    )
    .enablePlugins(
      ScalaJSPlugin,
      ScalaJSBundlerPlugin
    )

lazy val server = 
  (project in file("server"))
    .settings(commonSettings)
    .settings(
      name := ServerName,
      ThisBuild / run / fork := true,
      Test / fork := true,
      ThisBuild / version ~= (_.replace('+', '-')),
      Docker / packageName := ServerName,
      dockerUpdateLatest := true,
      dockerUsername := Some("peterstormio"),
      dockerBaseImage := "adoptopenjdk/openjdk11:alpine-jre",
      makeBatScripts := Seq(),
      libraryDependencies ++= Seq(
        "org.typelevel" %% "cats-core" % CatsVersion,
        "org.typelevel" %% "cats-effect" % CatsEffectVersion,
        "io.circe" %% "circe-core" % CirceVersion,
        "io.circe" %% "circe-extras" % CirceVersion,
        "io.circe" %% "circe-generic" % CirceVersion,
        "io.circe" %% "circe-parser" % CirceVersion,
        "io.circe" %% "circe-jawn" % CirceVersion,
        "co.fs2" %% "fs2-core" % Fs2Version,
        "org.tpolecat" %% "doobie-core" % DoobieVersion,
        "org.tpolecat" %% "doobie-hikari" % DoobieVersion,
        "org.typelevel" %% "log4cats-slf4j" % Log4CatsVersion,
        "ch.qos.logback" % "logback-classic" % LogBackVersion,
        "org.http4s" %% "http4s-dsl" % Http4sVersion,
        "org.http4s" %% "http4s-circe" % Http4sVersion,
        "org.http4s" %% "http4s-ember-server" % Http4sVersion,
        "org.http4s" %% "http4s-blaze-client" % Http4sVersion,
        "org.http4s" %% "http4s-ember-client" % Http4sVersion,
        "com.github.pureconfig" %% "pureconfig-core" % PureConfigVersion,
        "org.flywaydb" % "flyway-core" % FlywayVersion,
        "org.scalameta" %% "munit" % MunitVersion % Test,
        "org.typelevel" %% "munit-cats-effect-3" % MunitCatsEffectVersion % Test,
        "com.dimafeng" %% "testcontainers-scala-munit" % TestContainersScalaVersion % Test,
        "com.dimafeng" %% "testcontainers-scala-oracle-xe" % TestContainersScalaVersion % Test,
        "org.typelevel" %% "scalacheck-effect-munit" % ScalaCheckEffectVersion,
        "org.scalameta" %% "munit-scalacheck" % MunitVersion,
        //"com.github.pureconfig" %% "pureconfig-cats-effect" % PureConfigVersion
      ),
    )
    .enablePlugins(DockerPlugin)
    .enablePlugins(AshScriptPlugin)
