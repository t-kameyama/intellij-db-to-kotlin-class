# intellij-db-to-kotlin-class

![Build](https://github.com/t-kameyama/intellij-db-to-kotlin-class/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/18611-database-to-kotlin-class.svg)](https://plugins.jetbrains.com/plugin/18611-database-to-kotlin-class)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/18611-database-to-kotlin-class.svg)](https://plugins.jetbrains.com/plugin/18611-database-to-kotlin-class)

<!-- Plugin description -->
IntelliJ plugin that generates Kotlin classes from database tables

## Usages
- Select tables in the Database view and run "Generate Kotlin Data Class" action
- The action saves the generated code to your clipboard
- You can configure the plugin settings
  - <kbd>Settings/Preferences</kbd> > <kbd>Tools</kbd> > <kbd>Database to Kotlin Class</kbd>
<!-- Plugin description end -->

![test](https://user-images.githubusercontent.com/1121855/154066227-5192972d-5a8a-4049-8460-f28a2304ad04.gif)

## Installation
- Using IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "Database to Kotlin Class"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/t-kameyama/intellij-db-to-kotlin-class/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>


---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
