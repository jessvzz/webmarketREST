/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.univaq.swa.webmarket.rest.business;

/**
 *
 * @author jessviozzi
 */
public class ProposteServiceFactory {
   private final static ProposteServiceImpl service = new ProposteServiceImpl();
    
    public static ProposteServiceImpl getProposteService() {
        return service;
    } 
}
