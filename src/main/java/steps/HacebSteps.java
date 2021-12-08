package steps;


import archivoexcel.ArchivoExcel;
import drivers.GoogleChromeDriver;
import org.junit.Assert;
import org.openqa.selenium.By;
import pages.HacebPages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;


public class HacebSteps {

    HacebPages hacebPages = new HacebPages();
    ArchivoExcel lecturaExcel = new ArchivoExcel();

    public void abrirPagina(){GoogleChromeDriver.chromeWebDriver("https://www.haceb.com/");}


    public void BuscarElementoEnHaceb (String producto){

        GoogleChromeDriver.driver.findElement(hacebPages.getTxtBuscador()).sendKeys(producto);
        GoogleChromeDriver.driver.findElement(hacebPages.getBtnBuscador()).click();
        hacebPages.setBtnElementoBusqueda(producto);
        GoogleChromeDriver.driver.findElement(hacebPages.getBtnElementoBusqueda()).click();

    }



    public void buscarElementoEnHacebs(String productos)  {
        try {
            escribirEnTexto(hacebPages.getTxtBuscador(), productos);
            Thread.sleep(1000);
            clicEnElemento(hacebPages.getBtnBuscador());
            Thread.sleep(1000);
            hacebPages.setBtnElementoBusqueda(productos);
            Thread.sleep(1000);
            clicEnElemento(hacebPages.getBtnElementoBusqueda());
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public void validarElementoEnPantalla(String producto){
        hacebPages.setTxtElementoBusqueda(producto);
        Assert.assertEquals(producto,GoogleChromeDriver.driver.findElement(hacebPages.getTxtElementoBusqueda()).getText());

    }

    public void Busqueda_Validacion_Excel () throws IOException {

        ArrayList<Map<String, String>> listaProductos;
        listaProductos = lecturaExcel.leerDatosDeHojaDeExcel("Libro1.xlsx","Hoja1");
        for (Map<String, String> datos: listaProductos){
            String producto = datos.get("Productos");
            buscarElementoEnHacebs(producto);
            validarElementoEnPantalla(producto);
        }
    }




    public void escribirEnTexto(By elemento, String texto) {GoogleChromeDriver.driver.findElement(elemento).sendKeys(texto);}

    public void clicEnElemento(By elemento) {GoogleChromeDriver.driver.findElement(elemento).click();}



}
