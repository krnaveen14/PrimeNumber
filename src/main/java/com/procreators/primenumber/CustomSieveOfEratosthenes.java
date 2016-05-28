package com.procreators.primenumber;

import java.util.BitSet;
import java.util.Scanner;

/**
 * Prime Numbers Generator Upto N using 
 * Customized Sieve of Eratosthenes
 * 
 * @author Naveen Kumar
 */
public class CustomSieveOfEratosthenes {

    private BitSet primes;
    private int    limit;
    private int    sqrtLimit;
    private long   start;

    public static void main(String[] args) {

        CustomSieveOfEratosthenes es = new CustomSieveOfEratosthenes();
        es.doSetup();
        es.generate();

    }

    private void doSetup() {

        Scanner scanner = new Scanner(System.in);
        printn("-------------------- PRIME NUMBERS GENERATOR --------------------");
        boolean incorrectInput = true;
        do {
            printfn("Prime numbers can only generated upto [%d]", Integer.MAX_VALUE - 1);
            print("Generate Prime Numbers upto : ");
            try {
                limit = Integer.parseInt(scanner.next());
                if( limit == Integer.MAX_VALUE )
                    throw new Exception();
                incorrectInput = false;
            } catch (Exception ne) {
                printn("Incorrent Input... Please enter Valid number");
                printn("             ___________________________            ");
            }
        } while (incorrectInput);
        scanner.close();
        sqrtLimit = (int) Math.ceil(Math.sqrt(limit));
        primes = new BitSet(limit + 1);

    }

    private void generate() {

        start = System.nanoTime();

        {
            // Set 2,3,5,7 as Primes
            int[] definitePrimes = {2, 3, 5, 7};
            for(int definitePrime: definitePrimes) {
                if( definitePrime > limit )
                    break;
                primes.set(definitePrime);
            }
        }

        int prime = 7;
        // Set POSSIBLE Primes by Ignoring multiples of 2, 3, 5
        while (prime + 30L <= limit) {
            primes.set(prime += 4);
            primes.set(prime += 2);
            primes.set(prime += 4);
            primes.set(prime += 2);
            primes.set(prime += 4);
            primes.set(prime += 6);
            primes.set(prime += 2);
            primes.set(prime += 6);
        }
        if( prime <= limit && prime + 4L <= limit ) {
            long[] patterns = { 4, 2, 4, 2, 4, 6, 2, 6};
            for(int i=0; i<patterns.length && prime + patterns[i] <= limit; i++)
                primes.set(prime += patterns[i]);
        }

        /*
         * Now the values marked as Definite primes are 2 3 5 7
         * 
         * Values marked as POSSIBLE primes are 
         *  11  13  17  19  23  29  31  37
         *  41  43  47  49  53  59  61  67
         *  71  73  77  79  83  89  91  97
         * 101 103 107 109 113 119 121 127
         * 131 133 137 139 143 149 151 157
         * 161 163 167 169 173 179 181 187
         * 191 193 197 199 203 209 211 217
         * 221 223 227 229 233 239 241 247
         * 251 253 257 259 263 269 271 277
         * 281 283 287 289 293 299 301 307
         * 311 313 317 319 323 329 331 337
         * 341 343 347 349 353 359 361 367 ......
         * 
         * In above Possible primes, only non-primes were in the pattern of
         * DefinitePrime * DefinitePrime, 
         * DefinitePrime * DefinitePrime * DefinitePrime, 
         * DefinitePrime * DefinitePrime * DefinitePrime * DefinitePrime...
         * 
         * So starting from 7 as DefinitePrime, we clear the values in above
         * pattern as non-prime. After 7,
         * primes.nextSetBit(CurrentDefinitePrime+1) will give the next
         * DefinitePrime, coz any non-prime within that range is already marked
         * as non-prime by previous DefinitePrime(s) pattern clearence
         * 
         */
        
        // This logic is highly efficient as any number marked as non-prime 
        // will be marked as non-prime only once.
        //
        // i.e., primes.clear(multiple) -> 
        // Value of multiple will always be unique comparing with previous multiple values,
        // Value of multiple will never repeat.
        long multiple;
        for (prime = 7; prime <= sqrtLimit; prime = primes.nextSetBit(prime + 1)) {
            int cPrime = prime;
            // Clear non-primes starting from the Square of the Prime
            for (multiple = prime * prime; multiple <= limit && multiple > 0; multiple = prime * (cPrime = primes.nextSetBit(cPrime + 1))) {
                primes.clear((int) multiple);
                // Also clear (multiple * prime) as non-prime
                while ((multiple *= prime) <= limit) {
                    primes.clear((int) multiple);
                }
            }
        }

        long end = System.nanoTime();
        float ms = (float) ((end - start) / 1000000.0);
        float  s = (float) (ms / 1000.0);
        float kb = (float) ((limit / 8) / 1024.0);
        float mb = kb / 1024;
        
        printfn("Total Primes upto %d : %d", limit, primes.cardinality());
        printfn("Total Time Taken     : %.2f ms (or) %.2f s", ms, s);
        printfn("Total Memory Used    : %.2f KB (or) %.2f MB (Approx)", kb, mb);
    }

    void print(String msg) {
        System.out.print(msg);
    }

    void printn(String msg) {
        System.out.println(msg);
    }

    void printf(String msg, Object... args) {
        print(String.format(msg, args));
    }

    void printfn(String msg, Object... args) {
        printn(String.format(msg, args));
    }

}
