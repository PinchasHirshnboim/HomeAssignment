# HomeAssignment

An simple app, to show images according to a search word.

## Process flow

when app starts, the main activity is shown a search box to capture search word, and an empty RecyclerView.
once the search submited, it attempts to bring images from the api using the key search, and populate them in the RecyclerView.
every item in the list is able to show more details about its image.
![alt text](https://github.com/PinchasHirshnboim/HomeAssignment/blob/master/flow.jpg)

## problems I encountered

* when i try to change the recyclerview outside the mainUI thread, it is crashed.
* give to the recyclerview adapter ability to start a new activity.
* make the recycler view scrollable.
* and more.... :)

## things that you need to know if you want add this app to your production

now, url and parser is hardcoded to a specific api, if you want to add this to your project, you need to move the url and the parser to Environment Variables

## external librarys

okhttp - make the API calls.
picasso - load images from url into a ImageView.

### Dependencies

* android 5 and higher.

