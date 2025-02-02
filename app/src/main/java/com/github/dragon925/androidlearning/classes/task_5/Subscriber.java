package com.github.dragon925.androidlearning.classes.task_5;


/**
 * Класс Абонент: Идентификационный номер, Фамилия, Имя, Отчество, Адрес,
 * Номер кредитной карточки, Дебет, Кредит, Время междугородных и городских переговоров;
 * <p>
 * Конструктор;
 * <p>
 * Методы: установка значений атрибутов, получение значений атрибутов,
 * вывод информации. Создать массив объектов данного класса.
 * <p>
 * Вывести сведения относительно абонентов, у которых время городских переговоров
 * превышает заданное.  Сведения относительно абонентов, которые пользовались
 * междугородной связью. Список абонентов в алфавитном порядке.
 * (SubscriberListTest)
 */
public class Subscriber {

    private int id;
    private String lastName;
    private String firstName;
    private String middleName;
    private String address;
    private String creditCardNumber;
    private double debit;
    private double credit;
    private long cityCallTime;
    private long longDistanceCallTime;

    public Subscriber(int id,
                      String lastName,
                      String firstName,
                      String middleName,
                      String address,
                      String creditCardNumber,
                      double debit,
                      double credit,
                      long cityCallTime,
                      long longDistanceCallTime) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.address = address;
        this.creditCardNumber = creditCardNumber;
        this.debit = debit;
        this.credit = credit;
        this.cityCallTime = cityCallTime;
        this.longDistanceCallTime = longDistanceCallTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public double getDebit() {
        return debit;
    }

    public void setDebit(double debit) {
        this.debit = debit;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public long getCityCallTime() {
        return cityCallTime;
    }

    public void setCityCallTime(long cityCallTime) {
        this.cityCallTime = cityCallTime;
    }

    public long getLongDistanceCallTime() {
        return longDistanceCallTime;
    }

    public void setLongDistanceCallTime(long longDistanceCallTime) {
        this.longDistanceCallTime = longDistanceCallTime;
    }

    public void printInfo() {
        System.out.println("ID: " + id);
        System.out.println("Last Name: " + lastName);
        System.out.println("First Name: " + firstName);
        System.out.println("Middle Name: " + middleName);
        System.out.println("Address: " + address);
        System.out.println("Credit Card Number: " + creditCardNumber);
        System.out.println("Debit: " + debit);
        System.out.println("Credit: " + credit);
        System.out.println("City Call Time: " + cityCallTime);
        System.out.println("Long Distance Call Time: " + longDistanceCallTime);
    }
}
