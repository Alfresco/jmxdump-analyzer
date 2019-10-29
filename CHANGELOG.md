# Changelog

All notable changes to this project will be documented in this file.

## [Unreleased]

### Changed
- Created CHANGELOG.md as a replacement for Maven changes plugin
### Removed


## [2.1.4] - 2019-10-18

### Added

- Added Drag and dropping of zip file [@sirReeall](https://github.com/sirReeall)
    Users can now drag and drop jmx zip files on to the GUI, just like extracted txt files

## [2.1.3] - 2017-07-21

### Fixed

- Fix rubbish calculation of approximate docs in the index. Now using the isolation of the index subsystem to determine what to look for... [@astrachan](https://github.com/astrachan)

## [2.1.2] - 2017-03-15

### Changed

- Cleaned up UI [@astrachan](https://github.com/astrachan)

## [2.1.1] - 2017-02-20

### Added

- Extract start-time and up-time from JVM runtime variables, then calculate them into human readable form (Basics tab) [@astrachan](https://github.com/astrachan)

## [2.1.0] - 2017-02-17

### Added

- Updated for 5.2, plenty of additions and cleaning up UI [@astrachan](https://github.com/astrachan)
- serverInfo and readonly to basic information [@astrachan](https://github.com/astrachan)
- New 'Java' tab for java-specific system info [@astrachan](https://github.com/astrachan)

### Changed

- Separated AMP information in the AMP tab - much easier on the eyes [@astrachan](https://github.com/astrachan)
- Made the index information specific to the current index.subsystem, instead of listing EVERYTHING that pattern matches [@astrachan](https://github.com/astrachan)
- Tidied amps, populated cron properly [@astrachan](https://github.com/astrachan)

## [2.0.4] - 2016-03-23

### Added

- Implemented font increase and decrease, non-modal windows as well as corrected a few things

## [2.0.3] - 2016-03-11

### Added

- Implemented additional settings into the tables, added customer label grepped from license info

## [2.0.2] - 2016-02-05

### Added

- Implemented version information (Tools -> Version info)

## [2.0.1] - 2016-02-02

### Added

- Implemented feature for pressing ENTER for searching

## [2.0.0] - 2016-01-28

### Added

- Updated for 5.1, introduced DnD for dump files and opening ZIP files

## [1.1.0 - 1.0.5] - 2015-03-04 - 2015-02-25

### Added

- Added Transformation Server license info
- Added SOLR cache analysis menuItem
    Added additional modal page, a SOLR cache breakdown (if present)
- jmxdump-analyzer-fx Initial Mavenization and deployment

### Changed

- Corrected font
    Made sure the fixed-width tab font works on all platforms (now using monospaces-regular)
- Fixed bug in searching JVM tab information
- Tidied up UI slightly

[unreleased]: https://github.com/Alfresco/jmxdump-analyzer/tree/development
[2.1.4]: https://github.com/Alfresco/jmxdump-analyzer

Acknowledgements

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).