## TODO
1. [x] don’t store duplicate deeplinks
2. [ ] fix Material You not working on samsung devices
3. [ ] highlight searched substring
4. [x] on tapping the search button on the keyboard (`KeyboardActions.onSearch`), input should blur and keyboard should dismiss.

## Maybe TODO
1. [x] keep a track of search history
    1. [x] show the search history initially, rather than deeplinks
        1. don’t show anything at all if search history doesn’t seem right
2. revamp the UI
    1. replace the ‘Open Deeplink’ button with a inline button in the text input itself
    3. a delete mode instead of delete button on each item
        1. delete > deeplinks become checkbox selectable > delete the required one
    4. restrict deeplinks to a set amount of lines. Introduce a chevron button inplace of copy/delete to expand/collapse the deeplink.
        1. remove delete and place the chevron to left of copy? Hide/show chevron only for required deeplinks?
    5. [x] wrap the whole list in a card ala One UI 7 lists in contacts app
