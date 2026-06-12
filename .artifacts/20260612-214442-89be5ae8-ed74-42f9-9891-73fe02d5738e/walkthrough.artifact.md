# Walkthrough - Filled Spell Counts for Spellcasting Classes

I have populated the `spells` property for all spellcasting classes in the project. This property now correctly reflects the number of spells a character can know or prepare at each level (1-20), based on D&D 5e rules.

## Changes

I updated the following classes:

- **Bard**: Used "Spells Known" table (4 to 22 spells).
- **Sorcerer**: Used "Spells Known" table (2 to 15 spells).
- **Warlock**: Used "Spells Known" table (2 to 15 spells).
- **Ranger**: Used "Spells Known" table (2 to 11 spells, starting at level 2).
- **Cleric, Druid, Wizard**: Implemented "Prepared Spells" formula (Level + Modifier). Assumed a standard ability modifier progression (+3 at level 1 to +5 at level 20).
- **Paladin, Artificer**: Implemented "Prepared Spells" formula (Half Level + Modifier). Assumed the same modifier progression.

### Implementation Detail
The `spells` property is a `List<List<Int>>`. For each level, I provided a list where the total number of spells known/prepared is available at every valid spell level index. For example, if a Level 3 Bard knows 6 spells, `spells[3]` is `[0, 6, 6]`, meaning the limit is 6 for both Level 1 and Level 2 spells (as they share the same pool of "Known" spells in 5e).

## Verification Results

### Automated Tests
- Verified that the project builds successfully with `./gradlew :app:assembleDebug`.
- Confirmed that the `spells` list for each class contains 21 entries (for levels 0 to 20).

### Manual Verification
- Inspected the code structure to ensure `spells[level][spellLevel]` correctly maps to the total capacity for that level.
