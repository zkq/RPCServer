const MAXNAMELEN = 2048;
typedef byte RpcBitmap<>;
program TEST_RPC_FUNCTION_NUMBER
{
    version TEST_RPC_FUNCTION_VERSION
    {
        RpcBitmap getImg(String) = 1;
        String getFileTree(void) = 2;

    } = 1;
} = 0x20000001;