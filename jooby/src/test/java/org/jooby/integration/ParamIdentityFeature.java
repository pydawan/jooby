package org.jooby.integration;

import static org.junit.Assert.assertEquals;

import org.apache.http.client.fluent.Request;
import org.jooby.test.ServerFeature;
import org.junit.Test;

public class ParamIdentityFeature extends ServerFeature {

  public static class ParamBean {
  }

  {

    param((type, values, ctx) -> {
      if (type.getRawType() == ParamBean.class) {
        return new ParamBean();
      }
      return ctx.convert(type, values);
    });

    get("/identity", req -> {
      ParamBean call1 = req.param("p").to(ParamBean.class);
      ParamBean call2 = req.param("p").to(ParamBean.class);
      ParamBean call3 = req.param("p").to(ParamBean.class);
      ParamBean call4 = req.param("p").to(ParamBean.class);
      assertEquals(call1, call2);
      assertEquals(call2, call3);
      assertEquals(call3, call4);

      String callb1 = req.param("p").stringValue();
      String callb2 = req.param("p").stringValue();
      String callb3 = req.param("p").stringValue();
      String callb4 = req.param("p").stringValue();
      assertEquals(callb1, callb2);
      assertEquals(callb2, callb3);
      assertEquals(callb3, callb4);
      return "OK";
    });

  }

  @Test
  public void identity() throws Exception {
    assertEquals("OK", Request.Get(uri("identity?p=jooby").build()).execute().returnContent()
        .asString());
  }

}
