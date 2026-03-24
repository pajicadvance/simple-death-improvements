# Simple Death Improvements

Dying in Minecraft can be quite annoying, and progress loss caused by unlucky circumstances is nothing short of frustrating. This mod aims to remedy most (if not all) of these problems by introducing non-intrusive, vanilla friendly tweaks to player death. The mod is made almost exclusively to provide an alternative to (in my opinion) bloated gravestone mods that are out there. Everything is configurable, so you can pick and choose what you like.

Fabric version requires [Fabric API](https://modrinth.com/mod/fabric-api) and [Fzzy Config](https://modrinth.com/mod/fzzy-config). Use [Mod Menu](https://modrinth.com/mod/modmenu) to change settings in-game.

NeoForge version requires [Fzzy Config](https://modrinth.com/mod/fzzy-config). Use the Mods screen to change settings in-game.

## Features

### Items dropped on death never despawn

Items that you drop on death will never despawn, and will stay at the same place they dropped at forever. This gets rid of stressful runbacks to get your stuff back and lets you plan to recover your items without worrying about them disappearing.

### No item and experience splatter

When you die, your items and experience will drop condensed all together at the exact same spot you died at, instead of splattering all over the place. You no longer have to search for your items even after you arrive at your death point, and there's no longer a chance for your items to fly off into nearby lava.

### Lava and the Void item safeguarding

When you die in a place where your items are not safe to be dropped (in or above lava or the Void), your items will drop at the last known nearest "safe spot" that you walked over instead, preserving all of your items. This essentially gets rid of every death which causes massive loss of progress, as your items no longer get destroyed, although, if you disable No item splatter, some of your items may still end up being destroyed.

### Explosion resistant items

Explosions will no longer affect dropped items. The mechanic is largely useless and when it happens to destroy your dropped items (especially during fights with the Wither) it's extremely frustrating.

### Lose less experience on death

You will now lose way less experience compared to vanilla when you die (default 20%). The experience loss in vanilla is too high for no good reason.

### Keep items on death (off by default)

Choose whether to keep armor items, hotbar items or accessories equipped using [Accessories API](https://modrinth.com/mod/accessories) on death in the mod configuration. These settings are ignored if the keep inventory gamerule is toggled on.
