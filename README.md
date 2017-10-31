Markov chains are fun
===

[![Build Status](https://travis-ci.org/devshorts/markov.svg?branch=master)](https://travis-ci.org/devshorts/markov)

Markov chains build a probability map representing a state machine of the next word.

The goal is to parse the corpus with a chain size (lets say 2) which maps a sliding window across
the input data. The input data needs to be delimited by sentences, so first we tokenize the input data, removing punctuation, etc.

We also want to keep track of phrases that indicate "start" phrases, as we can use these start phrases as the input set to a state 
transition.

Once state system is created, we can leverage the weights on the input phrases to arbitrarily choose the next state. 