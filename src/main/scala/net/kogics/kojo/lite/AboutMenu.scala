package net.kogics.kojo
package lite

import net.kogics.kojo.util.Utils

import java.awt.event.{ActionEvent, ActionListener}
import java.awt.{Component, Dimension, Insets}
import javax.swing.text.html.HTMLEditorKit
import javax.swing._

class AboutMenu(kojoCtx: core.KojoCtx) {
  val frame = kojoCtx.frame
  val about = new JMenuItem(Utils.loadString("S_About"))
  about.addActionListener(new ActionListener {
    def actionPerformed(ev: ActionEvent): Unit = {
      val aboutBox = new JDialog(frame)
      val aboutPanel = new JPanel
      aboutPanel.setLayout(new BoxLayout(aboutPanel, BoxLayout.Y_AXIS))

      val kojoIcon = new JLabel()
      kojoIcon.setIcon(Utils.loadIcon("/images/splash.png"))
      kojoIcon.setSize(430, 280)
      kojoIcon.setAlignmentX(Component.CENTER_ALIGNMENT)
      aboutPanel.add(kojoIcon)

      val aboutText = new JEditorPane
      aboutText.setEditorKit(new HTMLEditorKit)
      aboutText.setEditable(false)
      aboutText.setText(
        s"""<html><body>
<div style="font-size: ${12 + kojoCtx.screenDpiFontDelta}pt; font-family: Verdana, 'Verdana CE',  Arial, 'Arial CE', 'Lucida Grande CE', lucida, 'Helvetica CE', sans-serif; ">
              <strong>Kojo Lib</strong> ${Versions.KojoLibMajorVersion}<br/>
              Version: ${Versions.KojoLibVersion}  <em>${Versions.KojoLibRevision}</em><br/>
              Build date: ${Versions.KojoLibBuildDate}<br/>
              <em>Java version: ${Versions.JavaVersion}. Scala version: ${Versions.ScalaVersion}</em> <br/><br/>
              Copyright &copy; 2009-2020 Lalit Pant (pant.lalit@gmail.com) and the Kojo Dev Team.<br/><br/>
              Kojo Lib has been derived from the Kojo Learning Environment.  Please visit <em>http://www.kogics.net/kojo</em> for more information about Kojo.<br/><br/>
              <strong>Kojo</strong> Contributors:<ul>
               <li>Lalit Pant</li>
               <li>Bj\u00f6rn Regnell</li>
               <li>Peter Lewerin</li>
               <li>Phil Bagwell</li>
               <li>Tanu Nayal</li>
               <li>Vibha Pant</li>
               <li>Anusha Pant</li>
               <li>Nikhil Pant</li>
               <li>Sami Jaber</li>
               <li>Aditya Pant</li>
               <li>Jerzy Redlarski</li>
               <li>Saurabh Kapoor</li>
               <li>Mushtaq Ahmed</li>
               <li>Ilango</li>
               <li>Pierre Couillard</li>
               <li>Audrey Neveu</li>
               <li>Mikołaj Sochacki</li>
               <li>Eric Zoerner</li>
               <li>Jacco Huysmans</li>
               <li>Christoph Knabe</li>
               <li>Vipul Pandey</li>
               <li>Aleksei Loginov</li>
               <li>Massimo Maria Ghisalberti</li>
               <li>Luka Volaric</li>
               <li>Marcus Klang</li>
               <li>Bülent Başaran</li>
               <li>Guillermo Ovejero</li>
               <li>Alberto R.R. Manzanares</li>
              </ul>
              <strong>Kojo</strong> is licensed under The GNU General Public License (GPL). The full text of the GPL is available at: http://www.gnu.org/licenses/gpl.html<br/><br/>
              Kojo runs on the Java Platform.<br/><br/>
              The list of third-party software used by <strong>Kojo</strong> 2.x includes:
              <ul>
              <li>The Scala Programming Language (http://www.scala-lang.org)</li>
              <li>Docking Frames (http://dock.javaforge.com/) for providing multiple, dockable windows</li>
              <li>RSyntaxTextArea (http://fifesoft.com/rsyntaxtextarea/) for Syntax Highlighting and Code Completion within the Script Editor</li>
              <li>Scalariform (https://github.com/mdr/scalariform/) for Code Formatting within the Script Editor</li>
              <li>Piccolo2D (http://www.piccolo2d.org) for 2D Graphics</li>
              <li>JTS Topology Suite (http://tsusiatsoftware.net/jts/main.html) for Collision Detection</li>
              <li>JFugue (http://www.jfugue.org) for computer generated music</li>
              <li>The H2 Database Engine (http://www.h2database.com) for storing history</li>
              <li>GeoGebra (http://www.geogebra.org) for Interactive Geometry and Algebra</li>
              <li>HttpUnit (http://httpunit.sourceforge.net/) for HTTP communication</li>
              <li>JLaTeXMath (http://forge.scilab.org/index.php/p/jlatexmath/) to display LaTeX commands</li>
              <li>JLayer (http://www.javazoom.net/javalayer/javalayer.html) to play MP3 files</li>
              <li>Table Layout (https://java.net/projects/tablelayout) for Arithmetic Aerobics</li>
              <li>The Netbeans Platform (http://www.netbeans.org) for some Script Editor Icons</li>
              <li>The Scratch Project (http://scratch.mit.edu) for some Media files</li>
              <li>The OpenJDK Project (http://openjdk.java.net/) for Tracing support</li>
              <li>JHLabs image filters (http://www.jhlabs.com/ip/filters/) for Picture effects</li>
              <li>jSSC (http://code.google.com/p/java-simple-serial-connector/) for serial port communication</li>
              <li>The Gargi font (http://savannah.nongnu.org/projects/gargi) for Devanagari support</li>
              <li>The Doodle project (https://github.com/underscoreio/doodle) for rich color support</li>
              <li>ScalaSwingContrib (https://github.com/benhutchison/ScalaSwingContrib) for UI layout</li>
              <li>Scalatest (http://www.scalatest.org/) for testing</li>
              <li>Akka (https://akka.io/) for concurrency</li>
              <li>Darcula (https://github.com/bulenkov/Darcula) for the dark Kojo theme</li>
              <li>libTiled (https://www.mapeditor.org/) to load game level files created with the Tiled Map Editor</li>
              <li>Processing (https://processing.org/) for perlin noise and curved shapes</li>
              <li>Delaunay Triangulator (https://github.com/jdiemke/delaunay-triangulator) for triangulation of points</li>
              <li>Java implementation of HSLuv (https://github.com/hsluv/hsluv-java) for a perceptually uniform color space</li>
              <li>Rhino (https://developer.mozilla.org/en-US/docs/Mozilla/Projects/Rhino) for the Code Exchange interface</li>
              </ul>
              </div>
              </body></html>
              """)
      aboutText.setPreferredSize(new Dimension(650, 300))
      aboutText.setMaximumSize(new Dimension(650, 300))
      aboutText.setCaretPosition(0)
      aboutText.setMargin(new Insets(15, 20, 15, 20))
      aboutPanel.add(new JScrollPane(aboutText))

      val verticalSpaceDim = new Dimension(1, 10)
      aboutPanel.add(Box.createRigidArea(verticalSpaceDim))

      val ok = new JButton(Utils.loadString("S_OK"))
      ok.setAlignmentX(Component.CENTER_ALIGNMENT)
      aboutBox.getRootPane.setDefaultButton(ok)
      ok.addActionListener(new ActionListener {
        def actionPerformed(ev: ActionEvent): Unit = {
          aboutBox.setVisible(false)
        }
      })
      aboutPanel.add(ok)

      aboutPanel.add(Box.createRigidArea(verticalSpaceDim))

      aboutBox.setModal(true)
      aboutBox.getContentPane.add(aboutPanel)
      aboutBox.pack()
      aboutBox.setLocationRelativeTo(frame)
      ok.requestFocusInWindow()
      Utils.closeOnEsc(aboutBox)
      aboutBox.setVisible(true)
    }
  })
  about.setIcon(Utils.loadIcon("/images/extra/about.gif"))
}
