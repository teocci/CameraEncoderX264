package com.github.teocci.videoencoder.interfaces;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2017/Apr/14
 */

public interface CommonGatewayInterface
{
    String run(Properties parms);

    InputStream streaming(Properties parms);
}
