# ChatQalc

https://www.curseforge.com/minecraft/mc-mods/chatqalc

An interface to [Qalculate! qalc](https://github.com/Qalculate/libqalculate#readme) - the best cli calculator. (Included for Windows/Linux)
![](https://camo.githubusercontent.com/a9c9f614b77ffdafc7b32741cbdd0a0b2288bb110c5bb3a1cba4c17e344ad24f/687474703a2f2f71616c63756c6174652e6769746875622e696f2f696d616765732f71616c632e706e67)

### Features
- Installs and runs portable interactive `qalc` cli calculator when you start Minecraft
- Operates in chat screen (`T` or `/`)
- `NumPadEnter` or `Ctrl+Enter` = Send message to qalc instead of chat
- `Ctrl+Tab` = Convert last expressions without spaces to calculated result (escape spaces with `\`)
- `Shift+Enter` = Send message to qalc and replace chat field with last line of the result (You can look at the result and decide to share it with everybody by pressing `Enter`)
- `Ctrl+Shift+Enter` = Same as `Ctrl+Enter`, but result is broadcast from your name to the whole server. And without colors. (Use when you want to share multiple lines)
- `Shift+Tab` = Get completions
- Chat history of your queries. Very helpful.
- Open config in ModMenu to get a graphical interface. All settings are there. (`Edit` tab)
- Some Minecraft-related units and variables (`100it = 1 stack + 36 items`) `it` `st` `sb` `pdc` `pdcwh` `bl` `in` `nu` `gt` `iph` `hs`, `ch`|`chunk`|`chunks`

### Qalculate! features
https://qalculate.github.io/features.html
Manual: https://qalculate.github.io/manual/qalc.html
Most notably:
- `/help` then `Ctrl+Enter`
- Use `to` for unit conversion (`1km to m = 1000 m`)
- Fault-tolerant parsing of strings (almost natural language)
- Ability to disable functions, variables, units or unknown variables for less confusion: e.g. when you do not want (a+b)^2 to mean (are+barn)^2 but ("a"+"b")^2
- Verbose error messages, Arbitrary precision
- Symbolic calculation
- Functions, Units, Variables, Constants (persistent between restarts)
- Gnuplot (opens a new window with plot, you have to install GNUplot separately for it to work)
- Comments start with `#`
