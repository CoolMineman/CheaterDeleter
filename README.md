[![build](https://github.com/CoolMineman/CheaterDeleter/actions/workflows/build.yml/badge.svg)](https://github.com/CoolMineman/CheaterDeleter/actions/workflows/build.yml)
![GitHub](https://img.shields.io/github/license/CoolMineman/CheaterDeleter)
# CheaterDeleter

A Minecraft Server-side Anti-Cheat for Fabric Servers. Detects and prevents many packet exploits, movement cheats, and other unwanted cheat mods.

## Pre-Alpha Quality Note

**This is currently pre-alpha quality. Expect problems.**

## Currently Includes Detection/Prevention for:

* Movement
    * Glide
    * Fly
    * VClip
    * HClip
    * Phase
    * Step (Even "legit" In Some Clients)
    * No Fall
    * Speed
    * XCarry and Inventory Move
    * Vehicle High Jump
    * Boat Fly
    * Poorly Written Inventory Item Movement Cheats (AutoTotem, AutoMlg if Bucket not in Hotbar)
    * Poorly Written Water Walk Cheats (Basically all the Public Ones)
    * Elytra Fly
* Packet Exploits
    * Timer/Extra Movement Packets
    * Timer While on Entity
    * Packet Limiter Stops Various DoS attacks
    * Crafting Packet Crash Exploit
    * Teleport Finder (Gives Fake Data Instead)
* Rotations
    * Spoofed Rotations (1 tick head snaps)
    * Lock on Entity (Used in KillAura Stay)
    * Clamped Yaw Spoof (lol)

## Supported Mods

* Step Height Entity Attribute Library [https://github.com/emilyalexandra/step-height-entity-attribute](https://github.com/emilyalexandra/step-height-entity-attribute)
* :key: fabric-permissions-api [https://github.com/lucko/fabric-permissions-api](https://github.com/lucko/fabric-permissions-api)
    * LuckPerms [https://github.com/lucko/LuckPerms](https://github.com/lucko/LuckPerms)

## Permissions

**Requires LuckPerms or Another fabric-permissions-api Compatable Permissions Mod**

`cheaterdeleter.bypassanticheat` - Allows a player to bypass this Anti-Cheat

`cheaterdeleter.sendmajorflags` - Send major flags from all players to this player in chat

`cheaterdeleter.sendminorflags` - Send minor flags from all players to this player in chat

## Discord

[https://discord.gg/JSZNtzRGjx](https://discord.gg/JSZNtzRGjx)

## Setup

For setup instructions please see the [fabric wiki page](https://fabricmc.net/wiki/tutorial:setup) that relates to the IDE that you are using.

## License

This anti-cheat mod is available under the CC0 license.
