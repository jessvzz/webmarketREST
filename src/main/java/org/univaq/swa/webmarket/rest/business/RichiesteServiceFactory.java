/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.univaq.swa.webmarket.rest.business;

/**
 *
 * @author jessviozzi
 */
public class RichiesteServiceFactory {
    private final static RichiesteServiceImpl service = new RichiesteServiceImpl();
    
    public static RichiesteServiceImpl getRichiesteService() {
        return service;
    }
}
