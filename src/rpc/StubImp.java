package rpc;

import model.FileTree;
import org.acplt.oncrpc.OncRpcException;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by Drew on 2016-12-13.
 */

public class StubImp extends rpcServerStub {

    private final static String FILE_ROOT = "c:/photos/";

    private StubImp(InetAddress bindAddr, int port) throws OncRpcException, IOException {
        super(bindAddr, port);
    }

    @Override
    public RpcBitmap getImg_1(String arg1) {
        System.out.println("getImg_1 " + arg1);
        File img = new File(FILE_ROOT + arg1);
        if(!img.exists()){
            return null;
        }

        byte[]  file = new byte[(int) img.length()];
        try {
            FileInputStream fis = new FileInputStream(img);
            fis.read(file);
            fis.close();
            return new RpcBitmap(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getFileTree_1() {
        System.out.println("getFileTree_1");
        File photosPath = new File(FILE_ROOT);
        FileTree fileTree = new FileTree(photosPath);
        return fileTree.toString();
    }


    public static void startServer(){
        InetAddress address = null;

        try {
            address = InetAddress.getByAddress(new byte[]{(byte) 192, (byte) 168, (byte) 0, (byte) 106});
        } catch (final UnknownHostException e) {
            e.printStackTrace();
            System.out.println("address error" + e.getMessage());
        }

        try {
            StubImp server = new StubImp(address, 2023);
            server.run();
        } catch (final OncRpcException e) {
            e.printStackTrace();
            System.out.println("make server error" + e.getMessage());
        } catch (final IOException e) {
            e.printStackTrace();
            System.out.println("make server error" + e.getMessage());
        }
    }


}
