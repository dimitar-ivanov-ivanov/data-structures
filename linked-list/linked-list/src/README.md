## Linked list

Data is usually represented as a node which contains the value of the element, but also a pointer to the next element.
The list can be doubly linked where every node points to the previous node as well.
Which means in the worst case you're storing the value, but also 2 pointers to memory.
Data is not sequentially stored in memory.
Unlike array or list reading from Linked list is slower you'll have to jump around in memory to find the data.

Searching is also slower you'll have to go through the list to find the wanted element - O(N) time complexity
Deleting and Inserting is much easier, if you have the pointer to the node - O(1) if you don't then it's O(N)
because you'll have to find the node, then make the new node point to the current node and make the current node
point to the new node.