package Mp3XML;

import java.util.List;

import mp3model.Mp3Info;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MP3ListContentHandler extends DefaultHandler{
	List<Mp3Info> infos = null;
	private Mp3Info info;
	
//	private String tagName;
	
	public List<Mp3Info> getInfos() {
		return infos;
	}

	public void setInfos(List<Mp3Info> infos) {
		this.infos = infos;
	}

	public MP3ListContentHandler(List<Mp3Info> infos) {
		super();
		this.infos = infos;
	}

	private boolean getId = false, getMp3Name = false, getMp3Size = false,
			getLrcName = false, getLrcSize = false;
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		
		if(getId){
			info.setId(new String(ch, start, length));
			getId = false;
		}else if(this.getMp3Name){
			info.setMp3Name(new String(ch, start, length));
			this.getMp3Name = false;
		}else if(this.getMp3Size){
			info.setMp3Size(new String(ch, start, length));
			this.getMp3Size = false;
		}else if(this.getLrcName){
			info.setLrcName(new String(ch, start, length));
			this.getLrcName = false;
		}else if(this.getLrcSize){
			info.setLrcSize(new String(ch, start, length));
			this.getLrcSize = false;
		}
		// TODO Auto-generated method stub
		super.characters(ch, start, length);
	}

	@Override
	public void endDocument() throws SAXException {
		System.out.println("Parse ended");
		// TODO Auto-generated method stub
		super.endDocument();
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if(localName.equals("resource")){
			infos.add(info);
		}
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
	}

	@Override
	public void startDocument() throws SAXException {
		System.out.println("Start Parsing");
		// TODO Auto-generated method stub
		super.startDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if(localName.equals("resource")){
			info = new Mp3Info();
		}else if(localName.equals("mp3.name")){
			this.getMp3Name = true;
		}else if(localName.equals("mp3.size")){
			this.getMp3Size = true;
		}else if(localName.equals("lrc.name")){
			this.getLrcName = true;
		}else if(localName.equals("lrc.size")){
			this.getLrcSize = true;
		}else if(localName.equals("id")){
			this.getId = true;
		}
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
	}
	
}
