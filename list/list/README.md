## List 
underneath a list is an array
this means that the data is sequential in memory.
when you create a list underneath it creates a buffer that allows for changing the size of the array.
Usually the buffer goes 4->8->16->32 where those numbers represent the max size of the list before it has to be extended.
the difference is that in the list you can create/delete elements.

creating element causes an extension of the list, it creates another array with more buffer, copies the old array
to the place of the new array, afterwards the data for the old array is garbage collected.
This can cause performance issues if the array is big, the bigger it is the slower the copy/paste to the new array will be.
At worst you adding an element can be O(N) which is the copy paste from the old to the new array.

if you delete elements that's better because you just have to decrease the size variable of the list.

if you create an empty list the buffer is 8 OR 16 so that's 8 OR 16 places in memory which is not used yet.