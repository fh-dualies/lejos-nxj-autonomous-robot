<h3>lejos-nxj-autonomous-robot</h3>
<p>Objektorientierte Systeme (Sommersemester 2025) - FH Münster</p>

---

## Overview

- [Members](https://github.com/fh-dualies/lejos-nxj-autonomous-robot#members)
- [About this repository](https://github.com/fh-dualies/lejos-nxj-autonomous-robot#about-this-repository)
- [Build & Upload](https://github.com/fh-dualies/lejos-nxj-autonomous-robot#build--upload)

## Members

- Lars Kemper
- Dennis Höllmann
- Tom Steinbach
- Floriane Monga

## About this repository

### Top-level layout

This repository's contents are divided across the following primary sections:

- `/assets` contains all assets used in the project.
- `/lib` contains all libraries used/needed in the project.
- `/out` contains all output files generated by the project.
- `/scripts` contains all scripts used in the project for building and formatting.
- `/src` contains all source code files for the project.

### Libs

- `nxt` lejos library for the NXT brick.
- `checkstyle` checkstyle library for Java.

### Architecture

|                               Core                                |                                   States Pattern                                   |
|:-----------------------------------------------------------------:|:----------------------------------------------------------------------------------:|
| ![core](/assets/diagrams/class-diagram/core-class-diagram.drawio.svg?raw=true) | ![states](/assets/diagrams/class-diagram/states-class-diagram.drawio.svg?raw=true) |

### Behavior

|                                     Sequence                                      |                                       States                                       |
|:---------------------------------------------------------------------------------:|:----------------------------------------------------------------------------------:|
| ![core](/assets/diagrams/behavior-diagram/sequence-diagram.drawio.svg?raw=true) | ![states](/assets/diagrams/behavior-diagram/state-diagram.drawio.svg?raw=true) |

## Build & Upload

Build the project without uploading to the NXT brick:

```sh
$ ./scripts/build.sh
```

Build the project with uploading to the NXT brick:

```sh
$ ./scripts/build.sh --upload
```