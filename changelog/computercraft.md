###Portal Modifier
| Method         | Description |
| -------------- | ----------- |
| createPortal() | |
| removePortal() | |
| getTexture()   | Gets the string representation of a texture. Will either be `C:0-15`, `B:<BLOCKID>:<META>` or `I:<ITEMID>:<META>` |
| getNetwork()   | Gets a table of all the glyph IDs used in the network |
| setNetwork(string) | Sets the network to the specified network. Must send a single string as an argument with the glyph IDs seperated by spaces `setNetwork("10 5 22 14 10")` |
| getIdentifier() | The same as `getNetwork()` but used when the modifier has a dialling upgrade |
| setIdentifier(string) | The same as `setNetwork(string)` but used when the modifier has a dialling upgrade |
| getThickness() | Returns an integer of the current thickness level, 1 = default, 4 = fullblock |
| setThickness() | Sets the thickness to a specified value, 1 = default, 4 = fullblock |
| getUpgrades() | Returns a table of all installed upgrade IDs |
| getUpgradeName(num) | Returns the name of the specified upgrade |
| getSelfActive() | Returns true if this modifier is active |
| getAnyActive() | Returns true if there is a portal on the active side of this block |
| getGlyphs() | Returns a table of all the Glyph IDs and their names |
| getGlyph(num) | Returns the specified glyphs name |

###Basic Dial Device
| Method         | Description |
| -------------- | ----------- |
| dial(string) | Dials the specified network. Single string with glyph ids seperated by a space. `dial("10 5 22 14 10")` |
| isActive() | Returns true if this dial device is active |
| getGlyphs() | Returns a table of all the Glyph IDs and their names |
| getGlyph(num) | Returns the specified glyphs name |

###Dial Device
| Method         | Description |
| -------------- | ----------- |
| dialStored(num) | Dials the identifier in the specified slot in the dialling device, the top one is 0 |
| terminate() | Terminates an active connection |
| setTimeoutTime(num) | Sets the time the connection stays active for. Must be between 19 and 1201 (ticks), where 19 and 1201 are both "don't automatically shutdown" |
| getDestinationCount() | Returns the amount of entries in the dial device's list |
| isActive() | Returns true if this dialling device is active |
| getGlyphs() | Returns a table of all the Glyph IDs and their names |
| getGlyph(num) | Returns the specified glyphs name |