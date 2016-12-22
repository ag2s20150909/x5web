package cn.liuyin.x5webmini.myunit;
import java.io.InputStream;
import org.apache.http.util.EncodingUtils;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.File;

public class MyFileTool
{
//	public String getRawString(int resourceid){
//	String res = ""; 
//	try{ 
//
//		//得到资源中的Raw数据流
//		InputStream in = getResources().openRawResource(resourceid); 
//
//		//得到数据的大小
//		int length = in.available();       
//
//		byte [] buffer = new byte[length];        
//
//		//读取数据
//		in.read(buffer);         
//
//		//依test.txt的编码类型选择合适的编码，如果不调整会乱码 
//		res = EncodingUtils.getString(buffer, "BIG5"); 
//
//		//关闭    
//		in.close(); 
//
//	}catch(Exception e){ 
//		e.printStackTrace();         
//	} 
//	return res;
// }
	
	//写数据到SD中的文件
	public void writeFileSdcardFile(String fileName,String write_str) throws IOException{ 
		try{ 

			FileOutputStream fout = new FileOutputStream(fileName); 
			byte [] bytes = write_str.getBytes(); 

			fout.write(bytes); 
			fout.close(); 
		}

		catch(Exception e){ 
			e.printStackTrace(); 
		} 
	} 


//读SD中的文件
	public String readFileSdcardFile(String fileName) throws IOException{ 
		String res=""; 
		try{ 
			FileInputStream fin = new FileInputStream(fileName); 

			int length = fin.available(); 

			byte [] buffer = new byte[length]; 
			fin.read(buffer);     

			res = EncodingUtils.getString(buffer, "UTF-8"); 

			fin.close();     
        } 

        catch(Exception e){ 
			e.printStackTrace(); 
        } 
        return res; 
	} 
	
	//读文件
	public String readSDFile(String fileName) throws IOException {  

	    String res="";
        File file = new File(fileName);  

        FileInputStream fis = new FileInputStream(file);  

        int length = fis.available(); 

		byte [] buffer = new byte[length]; 
		fis.read(buffer);     

		res = EncodingUtils.getString(buffer, "UTF-8"); 

		fis.close();     
		return res;  
	}  

//写文件
	public void writeSDFile(String fileName, String write_str) throws IOException{  

        File file = new File(fileName);  

        FileOutputStream fos = new FileOutputStream(file);  

        byte [] bytes = write_str.getBytes(); 

        fos.write(bytes); 

        fos.close(); 
	} 
	
//	String Name = File.getName();  //获得文件或文件夹的名称：
//	String parentPath = File.getParent();  //获得文件或文件夹的父目录
//	String path = File.getAbsoultePath();//绝对路经
//	String path = File.getPath();//相对路经 
//	File.createNewFile();//建立文件  
//	File.mkDir(); //建立文件夹  
//	File.isDirectory(); //判断是文件或文件夹
//	File[] files = File.listFiles();  //列出文件夹下的所有文件和文件夹名
//	File.renameTo(dest);  //修改文件夹和文件名
//	File.delete();  //删除文件夹或文件
	
	/**
     * 删除单个文件
     * @param   filePath    被删除文件的文件名
     * @return 文件删除成功返回true，否则返回false
     */
    public boolean deleteFile(String filePath) {
		File file = new File(filePath);
        if (file.isFile() && file.exists()) {
			return file.delete();
        }
        return false;
    }

    /**
     * 删除文件夹以及目录下的文件
     * @param   filePath 被删除目录的文件路径
     * @return  目录删除成功返回true，否则返回false
     */
    public boolean deleteDirectory(String filePath) {
		boolean flag = false;
        //如果filePath不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        //遍历删除文件夹下的所有文件(包括子目录)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
				//删除子文件
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } else {
				//删除子目录
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前空目录
        return dirFile.delete();
    }

    /**
     *  根据路径删除指定的目录或文件，无论存在与否
     *@param filePath  要删除的目录或文件
     *@return 删除成功返回 true，否则返回 false。
     */
    public boolean DeleteFolder(String filePath) {
		File file = new File(filePath);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile()) {
				// 为文件时调用删除文件方法
                return deleteFile(filePath);
            } else {
				// 为目录时调用删除目录方法
                return deleteDirectory(filePath);
            }
        }
    }
}
