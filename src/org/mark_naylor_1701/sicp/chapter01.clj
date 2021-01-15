;; author: Mark W. Naylor
;; file:  chapter01.clj
;; date:  2021-Jan-10
(ns org.mark-naylor-1701.sicp.chapter01)

;; Examples

;; Trivial, but used in later examples and exercises.
(defn square
  "Return the result of a number muliplied by itself."
  [x]
  (* x x))

(defn sum-of-squares
  ""
  [x y]
  (+ (square x) (square y)))


;; Although defined in Math/abs, use this version to follow the text.
(defn abs
  ""
  [x]
  (cond (< x 0) (- x)
        :else x))

;; Example 1.1.7: Square Roots by Newton's Method
(def ^:dynamic *delta* 0.001)

(defn good-enough?
  [guess x]
  (< (abs (- (square guess) x)) *delta*))

(defn average
  [x y]
  (/ (+ x y) 2.0))

(defn improve
  [guess x]
  (average guess (/ x guess)))

(defn sqrt-iter
  [guess x]
  (if (good-enough? guess x)
    guess
    (sqrt-iter (improve guess x)
               x)))

(defn sqrt
  [x]
  (sqrt-iter 1.0 x))

;; Exercise 1.2 Convert to prefix form
(def ex-1_2 (/
             (+ 5 4 (- 2 (- 3 (+ 6 4/5))))
             (* 3 (- 6 2) (- 2 7))))

;; Exercise 1.3 Define a procedure that takes three numbers and
;; returns the sum of the squares of the two larger numbers.
(defn ex-1_3
  ""
  [x y z]
  (cond (and (>= x z) (>= y z)) (sum-of-squares x y)
        (and (>= x y) (>= z y)) (sum-of-squares x z)
        :else (sum-of-squares y z)))


;; Exercise 1.6. Alyssa P. Hacker doesn't see why if needs to be provided
;; as a special form. "Why can't I just define it as an ordinary
;; procedure in terms of cond?" she asks. Alyssa's friend Eva Lu Ator
;; claims this can indeed be done, and she defines a new version of if:2

(defn new-if
  ""
  [predicate then-clause else-clause]
  (cond predicate then-clause
        :else     else-clause))

;; Eva demonstrates the program for Alyssa:
(new-if (= 2 3) 0 5)

(defn sqrt-iter-new-if
  [guess x]
  (new-if (good-enough? guess   x)
          guess
          (sqrt-iter-new-if (improve guess x)
                            x)))

;; Q: What happens if we run this?  A: Stack blows up because else clause always evaluated.

;; ------------------------------------------------------------------------------
;; BSD 3-Clause License

;; Copyright © 2021, Mark W. Naylor
;; All rights reserved.

;; Redistribution and use in source and binary forms, with or without
;; modification, are permitted provided that the following conditions are met:

;; 1. Redistributions of source code must retain the above copyright notice, this
;;    list of conditions and the following disclaimer.

;; 2. Redistributions in binary form must reproduce the above copyright notice,
;;    this list of conditions and the following disclaimer in the documentation
;;    and/or other materials provided with the distribution.

;; 3. Neither the name of the copyright holder nor the names of its
;;    contributors may be used to endorse or promote products derived from
;;    this software without specific prior written permission.

;; THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
;; AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
;; IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
;; DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
;; FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
;; DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
;; SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
;; CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
;; OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
;; OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
