package com.sohu.cas.client.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * @author LHB
 * @param <T>
 */
public class ConfigHandler {

	/**
	 * @param file
	 * @throws JAXBException
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public static <T> String toXML(T t) throws JAXBException, FileNotFoundException {

		JAXBContext jc = JAXBContext.newInstance((Class<T>)t.getClass());
		Marshaller marshaller = jc.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "GBK");

		StringWriter writer = new StringWriter();
		marshaller.marshal(t, writer);
		return writer.toString();
	}

	/**
	 * @param fileName
	 * @return
	 * @throws JAXBException
	 */
	public static <T> T fromXML(Class<T> entityClass, String fileName) throws JAXBException   {
		return fromXML(entityClass, new File(fileName));
	}


	@SuppressWarnings("unchecked")
	public static <T> T fromXML(Class<T> entityClass, File file) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(entityClass);
		Unmarshaller unmarshaller = jc.createUnmarshaller();

		return (T) unmarshaller.unmarshal(file);
	}


	@SuppressWarnings("unchecked")
	public static <T> T fromXML(Class<T> entityClass, InputStream stream) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(entityClass);
		Unmarshaller unmarshaller = jc.createUnmarshaller();

		return (T) unmarshaller.unmarshal(stream);
	}
}
