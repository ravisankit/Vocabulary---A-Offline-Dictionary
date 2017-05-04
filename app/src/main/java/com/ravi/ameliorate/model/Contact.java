package com.ravi.ameliorate.model;

/**
 * Created by Ankit on 13-01-2017.
 */

public class Contact {

    int id;
    String word;
    String meaning;
    String meaning2;
    String pronun;
    String synonym;
    String antonym;



    String sentence;


    public Contact(){

    }

    public Contact(int id, String name, String meaning){

        this.id = id;
        this.word = name;
        this.meaning = meaning;

    }

    public Contact(String name, String meaning){

        this.word = name;
        this.meaning = meaning;

    }

    public Contact(String name, String meaning,String meaning2,String pronun,
                   String synonym,String antonym,String sentence){

        this.word = name;
        this.meaning = meaning;
        this.meaning2 = meaning2;
        this.pronun = pronun;
        this.synonym = synonym;
        this.antonym = antonym;
        this.sentence = sentence;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return word;
    }

    public void setName(String name) {
        this.word = name;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning2() {
        return meaning2;
    }

    public void setMeaning2(String meaning2) {
        this.meaning2 = meaning2;
    }

    public String getPronun() {
        return pronun;
    }

    public void setPronun(String pronun) {
        this.pronun = pronun;
    }

    public String getSynonym() {
        return synonym;
    }

    public void setSynonym(String synonym) {
        this.synonym = synonym;
    }

    public String getAntonym() {
        return antonym;
    }

    public void setAntonym(String antonym) {
        this.antonym = antonym;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }



 }
