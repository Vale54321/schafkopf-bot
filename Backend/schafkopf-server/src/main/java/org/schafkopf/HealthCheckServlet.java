package org.schafkopf;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Class that represents one Frontend Connection.
 */
public class HealthCheckServlet extends HttpServlet {

  /**
   * Class that represents one Frontend Connection.
   */
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    resp.setContentType("text/plain");
    resp.getWriter().println("Backend server is up and running!");
  }
}
