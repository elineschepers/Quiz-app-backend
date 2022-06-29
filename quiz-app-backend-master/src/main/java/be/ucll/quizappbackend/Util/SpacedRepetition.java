/**
 * Copyright (c) 2022
 * <p>
 * long description for the file
 *
 * @summary short description for the file
 * @author Kevin Coorens <kevin.coorens@student.be>
 * <p>
 * Created at     : 23/02/2022 22:09
 */

package be.ucll.quizappbackend.Util;

import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;

public class SpacedRepetition {
    //input:
    // q = user grade eq 0 or 5
    // n = repetition = successfully recalled
    // ef = easiness factor = starting at 2.5
    // i = interval = time before question will be asked again

    //output:
    // n = repetition = successfully recalled
    // ef = easiness factor = starting at 2.5
    // i = interval = time before question will be asked again = starting from 0

    //how to read
    // high EF = very easy question aka got the question right a lot of times
    // low EF = very difficult question aka got the question wrong a lot of times
    // high i = it won't be asked again quickly, the higher i the longer it will take for the question to be asked again
    // low i = it will be asked again quickly, the lower  i the faster the question will be asked again

    public static HashMap<String,Double> calculate(double q, double n, double ef, double i) {

        HashMap<String,Double> res = new HashMap();
        if (q == 5) { // correct response

            if (n == 1) { //question is never asked before just this once

                i = 1;
            } else if (n == 2) { //question has only been asked once before and this time

                i = 6;
            } else { // question has been asked more than twice
                i = Math.round(i * ef);
            }
            //n+=1; //increase sucessfully recalled not needed
        } else { //incorrect response
            i = 1;
        }

        System.out.println();
        System.out.println("ADDED TO EF: " + (0.1 -( 5-q) *(0.08 + (5 -q) *0.02)));
        System.out.println("OLD EF equals: " + ef);

        ef = ef + (0.1 -( 5-q) *(0.08 + (5 -q) *0.02)); //sm2 algo

        if (ef < 1.3) {
            ef = 1.3;
        }

        System.out.println("NEW EF equals: " + ef);
        System.out.println("I equals: " + i);
        System.out.println("N equals: " + n);
        System.out.println("Q equals: " + q);
        System.out.println();

        res.put("q",q);
        res.put("ef",ef);
        res.put("i",i);
        res.put("n",n);

        return res;

    }
}
