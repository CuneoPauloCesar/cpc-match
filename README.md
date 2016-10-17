Pattern matching on values as data-structure
============================================

Pattern matching implemented through tries and ranges lists.
Given a list of rules, support equals, notEquals and strict range.

* Uses HashMap so keys/columns values will need hashcode and equals implementation.

Use case example
================ 

Given a table.

|Rule| Age                   | Gender | Degree   | Verdict |
|----| ----------------------|--------| ---------|---------|
|1   |  between 35  &  45    | ANY    | ANY      |REJECT   |
|2   |  between 20  &  35    | ANY    | DOC      |ACCEPT   |
|3   |  less than 20         | ANY    | BACHELOR |ACCEPT   |

Will construct a trie.

```
(Root) 
 + - (Age) [-inf , 20]
 |     + - (Gender) ANY
 |            + -(Degree) ANY 
 |                  + -(Verdict) REJECT
 + - (Age) [20, 35]
 |     + - (Gender) ANY
 |           + -(Degree) DOC 
 |                 + -(Verdict) ACCEPT
 + - (Age) [35, 45]
     + - (Gender) ANY
           + -(Degree) BACHELOR 
                 + -(Verdict) ACCEPT
```

Cyclomatic complexity
=====================

Equality and non equality lookup consist in making a path to the leaf.
The trie height is fixed (is equals to the number of columns) so the trie transversal
takes O(#columns * O(HashMap.get)) yielding O(1) on some cases.
 
RangeTrie is implemented with a sorted list, so lookup is O( log( NUMBER_of_RANGES ) )

O(lookup) ~ O(O(#columns * O(HashMap.get)) + O(log(max(NUMBER_RANGES)))) ~ O( 1 )

So for bigger tables it much better than doing a linear search.
 
Not Equals implementation
=========================
Each level of the trie besides the actual values inserted has an ANY and NOT nodes.

The NOT node contains the union of the NOT rules provided for the level.
When a not rule is inserted the value given is added to all subtries different from the given 
and different from ANY,
including the NOT sub-trie, if the value does not exist in the trie,
 a node for it is created with the previous values of the not node.
 
 Example: Using clojure notation for maps {} and sets #{}
 ```
 
insert(key -> val)

insert(1    -> a )

{ 1   #{a}
  not #{}}


insert(not(2)  -> b )

{ 1 #{a b} ; added b because 2 != 1
  2 #{}    ; previous not value
  not #{b} } ; not hold the union of nots

insert(not(3)  -> c )

{ 1 #{a b c} ; added c because 3 != 1
  2 #{c}     ; added c because 3 != 2
  3 #{b}     ; previous not value
  not #{b c} } ; not hold the union of nots
  
 ```
 
So each node contains all its values. And the Not node contains the not union.
During lookup if there is not a matching sub-trie, it will match the NOT-trie. 
 
API
===

The matching result is non deterministic, meaning it may yield more than one matching result.
Result filtering is meant to be handled by user.

* There is no type checking for the COL, if you want it wrap a facade/adapter around the trie.
* Each insertion must match the same number of columns, but its no being check as of now. 

```
import cpc.match.api.Trie; // Trie and Trie.Builder Interface.
import cpc.match.api.Matcher;
import static cpc.match.api.Matcher.*;

...

        Trie.Builder<Object> b = Trie.builder(); // Default Tree.builder

        // Value is the first argument so can use varargs for path.
        // b.insert(VALUE, COL_1_PATTERN, COL_2_PATTERN, ..., COL_n_PATTERN);
        
        b.insert(1, or(1), or(1));
        b.insert(2, or(1), or(2));
        b.insert(3, or(2), ANY);
        b.insert(4, or(3), nor(5));
        
        // builder is mutable so no fluent interface, sorry.
        
        // Build a trie
        Trie<Object> trie = b.build();
        // Accept the actual COL values to match.
        // trie.match(COL_1, COL_2, ..., COL_n) -> List 
        
        assertThat(trie.match(1, 1), is(asList(1)));
        // because it matches only:
        // b.insert(1, or(1), or(1));
        
        
```


Trie compression
================

When the builder builds the trie it may remove unused node, or replace an node with more specific node.
In particular if a trie has only a ANY node it with change it for bypass node.


TODO
====

* Make Literate Test/Improve test as documentation.
* Validate columns size.
* Optimize range insertion strict O(n) as of now.
* Optimize insertion with permutation (See clojure.core.match / Maranget).
* Support path removal in builder.
* Compile to byte code ?



Copyright & LICENSE
===================

Copyright (c)  Paulo César Cuneo. All right reserved.

The use and distribution terms for this software are covered by the
Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
which can be found in the file epl-v10.html at the root of this distribution.
By using this software in any fashion, you are agreeing to be bound by
the terms of this license.
You must not remove this notice, or any other, from this software.

 


  
  

