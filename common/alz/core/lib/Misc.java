package alz.core.lib;

import java.nio.ByteBuffer;

import org.bouncycastle.util.Arrays;

public class Misc
{
    public static int byteArrayToInteger(byte[] bytes)
    {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        return buffer.getInt();
    }

    public static byte[] concatByteArray(byte[] a, byte[] b)
    {
        byte[] c = new byte[a.length + b.length];

        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        
        return c;
    }
    
    public static Object[] concatObjectArray(Object[] a, Object[] b)
    {
        Object[] c = new Object[a.length + b.length];

        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);

        return c;
    }

    public static byte[] integerToByteArray(int num)
    {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(num);

        return buffer.array();
    }
    
    public static byte[] getByteArray(Object... objects)
    {
        byte[] byteArray = new byte[0];
        
        for (Object o : objects)
        {
            if (o instanceof Integer)
            {
                int obj = Integer.parseInt(o.toString());
                
                byteArray = concatByteArray(byteArray, integerToByteArray(obj));
            }
            else if (o instanceof String)
            {
                byte[] arr = o.toString().getBytes();
                
                byteArray = concatByteArray(byteArray, new byte[] { (byte) arr.length });
                byteArray = concatByteArray(byteArray, arr);
            }
            else if (o instanceof Byte)
            {
                byteArray = concatByteArray(byteArray, new byte[] { Byte.parseByte(o.toString()) });
            }
            else if (o instanceof Boolean)
            {
                boolean val = Boolean.parseBoolean(o.toString());
                
                byteArray = concatByteArray(byteArray, new byte[] { (byte) (val ? 1 : 0) });
            }
            else if (o instanceof Byte[])
            {
                byte[] bytes = (byte[]) o;
                
                byteArray = concatByteArray(byteArray, new byte[] { (byte) bytes.length });
                byteArray = concatByteArray(byteArray, bytes);
            }
            else
            {
                System.out.println("ERROR: Unknown type found: " + o);
            }
        }
                
        return byteArray;
    }
    
    public static Object[] getObjects(byte[] byteArray, String... objectsExpecting)
    {
        if (byteArray.length == 0)
        {
            System.out.println("ByteArray is null");
            return null;
        }
        
        try
        {
            Object[] returnArray = new Object[0];
            int byteArrayPosition = 0;
            
            for (String s : objectsExpecting)
            {
                if (s.equals("I"))
                {
                    returnArray = concatObjectArray(returnArray, new Object[] { byteArrayToInteger(Arrays.copyOfRange(byteArray, byteArrayPosition, byteArrayPosition + 4)) });
                    
                    byteArrayPosition += 4;
                }
                else if (s.equals("S"))
                {
                    int length = byteArray[byteArrayPosition];
                    byteArrayPosition += 1;
                    
                    if (length != 0)
                    {
                        byte[] stringArray = Arrays.copyOfRange(byteArray, byteArrayPosition, byteArrayPosition + length);                        
                        returnArray = concatObjectArray(returnArray, new Object[] { new String(stringArray, "UTF-8") });                        
                        byteArrayPosition += length;
                    }
                }
                else if (s.equals("B"))
                {
                    returnArray =  concatObjectArray(returnArray, new Object[] { byteArray[byteArrayPosition] == 1 });
                    byteArrayPosition += 1;
                }
                else if (s.equals("b"))
                {
                    returnArray = concatObjectArray(returnArray, new Object[] { byteArray[byteArrayPosition] });
                    byteArrayPosition += 1;
                }
                else if (s.equals("b[]"))
                {
                    int length = byteArray[byteArrayPosition];
                    byteArrayPosition += 1;
                    
                    if (length != 0)
                    {
                        byte[] bytes = Arrays.copyOfRange(byteArray, byteArrayPosition, byteArrayPosition + length);     
                        Object[] bytesArr = new Object[bytes.length];
                        
                        for (int i = 0; i < bytes.length; i++)
                        {
                            bytesArr[i] = bytes[i];
                        }
                        
                        returnArray = concatObjectArray(returnArray, bytesArr);                        
                        byteArrayPosition += length;
                    }
                }
            }
            
            return returnArray;
        }
        catch (Exception e)
        {
            System.out.println("ERROR: Failed to get all objects.");
            e.printStackTrace();
            return null;
        }
    }
}
