# Petri

Built in Android Gradle Verstion 2.8 and Android Plugin Version 1.5.0

Petri is a sandbox style application in which users can manipulate an enviornment of different "cells". 

## Green Cells
Green cells are initially generated with a random set of characteristics influencing their size, strength, speed, hunger and fear. Green cells will roam the environment freely and randomly and will favor a path towareds the neares food cell based on their hunger stat.
The fear stat will influence how likely a green cell is to fight or run from predators within a certain range. If a green cell decides to fight a predator cell, or is caught by a predator cell, each cell will deal damage to the other based on its strength
stat until one of the cells' health is depleted. Green cells can reproduce by collecting three food cells. When a cell reproduces, the child cell's stats are determined randomly using the parent cell's stats as the base for the range of new potential stats. This allows
future generations of cells to have similar characteristics based on the cells that best survive the predators.

## Orange Food Cells
Orange food cells are statically placed cells that the user can place throughout the enviornmnet. Predators will ignore the food cells, but green cells may gravitate towards food cells depending on their hunger stat.

## Predator Cells
Red predator cells will chase the nearest green cell until it eventually catches up to it and attempts to kill it. The initial speed, strength, and health stats are determined by the user so they may experimeent with different outcomes when introducing predators into
the environment.

## UI
The top UI will keep track of the average statistics of the environment as well as the longest generational line of green cells.

## Levels
Along with the default sandbox mode, several levels were in the works that would require the user to create an environment meeting certain conditions. These conditions included, reacing a certain generation of green cells, breeding a green cell capable of killing
a predator cell, and defending a large, slow moving, Queen cell. These levels ultimately went unfinished, though they still exist within the application, some more playable than others.
