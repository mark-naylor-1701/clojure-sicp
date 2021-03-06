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
(defn- abs
  ""
  [x]
  (cond (< x 0) (- x)
        :else x))

;; Example 1.1.7: Square Roots by Newton's Method
(def ^:private ^:dynamic *delta* 0.001)

(defn- good-enough-1?
  [guess x]
  (< (abs (- (square guess) x)) *delta*))

(defn- average
  [x y]
  (/ (+ x y) 2.0))

(defn- improve
  [guess x]
  (average guess (/ x guess)))

(defn- sqrt-iter-1
  [guess x]
  (if (good-enough-1? guess x)
    guess
    (recur (improve guess x)
               x)))

(defn sqrt-1
  [x]
  (sqrt-iter-1 1.0 x))

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


;; Because `new-if' is not a special form, the recursive call has to
;; made explicitly. Using recur fails in this case because the Clojure
;; compiler does not recognize as a proper tail-call.>
(defn sqrt-iter-new-if
  [guess x]
  (new-if (good-enough-1? guess   x)
          guess
          (sqrt-iter-new-if (improve guess x) x)))

;; Q: What happens if we run this?  A: Stack blows up because else clause always evaluated.


;; Exercise 1.7. The good-enough? test used in computing square roots
;; will not be very effective finding the square roots of very small
;; numbers. Also, in real computers, arithmetic operations are always
;; performed with limited precision. This makes our test inadequate
;; for very large numbers. these statements, with examples showing how
;; the test fails for small and large numbers. An strategy for
;; implementing good-enough? is to watch how guess changes from one
;; iteration to next and to stop when the change is a very small
;; fraction of the guess. Design a square-root procedure uses this
;; kind of end test. Does this work better for small and large
;; numbers?

(defn- good-enough-2?
  [guess x prior]
  (< (/ (abs (- guess prior)) guess) *delta*))

(defn- sqrt-iter-2
  ([guess x]
   (sqrt-iter-2 guess x -1.0))

  ([guess x prior]
   (if (good-enough-2? guess x prior)
     guess
     (recur (improve guess x)
                  x
                  guess))))

(defn sqrt-2
  [x]
  (sqrt-iter-2 1.0 x -1.0))

;; Exercise 1.8. Newton's method for cube roots is based on the fact
;; that if y is an approximation to the cube root of x, then a better
;; approximation is given by the value:

;; (x/y^2 + 2y) / 3

;; Use this formula to implement a cube-root procedure analogous to
;; the square-root procedure. (In section 1.3.4 we will see how to
;; implement Newton's method in general as an abstraction of these
;; square- root and cube-root procedures.)

(defn- cube
  "x raised to the 3rd power."
  [x]
  (* x x x))

(defn- cube-root-native
  "Cube root function using native Math/pow function."
  [x]
  (double (Math/pow x 1/3)))


(defn- improve-cube
  [guess x]
  (let [guess (double guess) ]
    (/ (+ (/ x (square guess)) (* 2 guess)) 3)))

(defn- good-enough-cube?
  [guess x]
  (< (abs (- (cube guess) x)) *delta*))

(defn- cube-root-iter
  [guess x]
  (if (good-enough-cube? guess x)
    guess
    (recur (improve-cube guess x)
           x)))

(defn cube-root
  ""
  [x]
  (cube-root-iter 1 x))


;;Exercise 1.10. The following procedure computes a mathematical function called Ackermann's function.
;; (define (A x y)
;;   (cond ((= y 0) )
;;         ((= x 0) (* 2 y))
;;         ((= y 1) 2)
;;         (else (A
;;                (- x 1)
;;                (A x (- y 1))))))

(defn A
  "Ackermann's function."
  [x y]
  (cond
    (zero? y) 0
    (zero? x) (* 2 y)
    (= y 1) 2
    :else (A (dec x) (A x (dec y)))))

;; What are the values of the following expressions?
;; (A 1 10)    -> 1024
;; (A 2 4)     -> 65536
;; (A 3 3)     -> 65536

;; Consider the following procedures, where A is the procedure defined above:
;; (define (f n) (A 0 n))
;; (define (g n) (A 1 n))
;; (define (h n) (A 2 n))
;; (define (k n) (* 5 n n))
;; Give concise mathematical definitions for the functions computed by the procedures f, g, and h for
;; positive integer values of n. For example, (k n) computes 5n2.

(def f "Partially applied A on 0" (partial A 0))
(def g "Partially applied A on 1" (partial A 1))
(def h "Partially applied A on 2" (partial A 2))
(defn k [n] "Computes 5 * n^2" (* 5 n n))

;;  Define Fibonacci variants

(defn fib
  "Recursive version."
  [x]
  (cond
    (zero? x) 0
    (= x 1) 1
    :else (+ (fib (dec x)) (fib (- x 2)))))

(defn fib-iter
  "Iterative (tail recursive) version."
  [x]

  (letfn [(fib-iter
             [a b count]
             (cond
               (zero? count) b
               :else (recur (+ a b) a (dec count))))]
    (fib-iter 1 0 x)))


;; Exercise 1.11. A function f is defined by the rule that f(n) = n if
;; n<3 and f(n) = f(n - 1) + 2f(n - 2) + 3f(n - 3) if n> 3. Write a
;; procedure that computes f by means of a recursive process. Write a
;; procedure that computes f by means of an iterative process.

(defn ex1-11-recur
  [x]
  (cond
    (< x 3) x
    :else (+ (ex1-11-recur (- x 1))
             (* 2 (ex1-11-recur (- x 2)))
             (* 3 (ex1-11-recur (- x 3))))))

;; Solution found here: http://community.schemewiki.org/?sicp-ex-1.11
;; (define (f n)
;;   (define (f-i a b c count)
;;     (cond ((< n 3) n)
;;           ((<= count 0) a)
;;           (else (f-i (+ a (* 2 b) (* 3 c)) a b (- count 1)))))
;;   (f-i 2 1 0 (- n 2)))

(defn ex1-11-iter
  [x]
  (letfn
      [(ex1-11-iter [a b c count]
         (cond
           (< x 3) x
           (<= count 0) a
           :else (recur
                  (+ a (* 2 b) (* 3 c))
                  a
                  b
                  (dec count))))]
    (ex1-11-iter 2 1 0 (- x 2))))

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
