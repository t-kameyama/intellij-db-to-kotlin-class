# intellij-db-to-kotlin-class

![Build](https://github.com/t-kameyama/intellij-db-to-kotlin-class/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)

<!-- Plugin description -->
IntelliJ plugin that generates Kotlin classes from database tables

## Usages
- Select tables in the Database view and run "Generate Kotlin Data Class" action
- The action saves the generated code to your clipboard
- You can configure the plugin settings
  - <kbd>Settings/Preferences</kbd> > <kbd>Tools</kbd> > <kbd>Database to Kotlin Class</kbd>
<!-- Plugin description end -->

![test](https://user-images.githubusercontent.com/1121855/153991625-f4ea3e16-16cf-4d72-8994-852cd33ce8de.gif)

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
