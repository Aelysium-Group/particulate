package net.noknt.showcontrol.database;

import net.noknt.showcontrol.ShowControl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LinkChannel {
    ShowControl showControl;
    public LinkChannel(ShowControl showControl) {this.showControl = showControl;}

    public boolean enableLinkChannel(int link_id,int status, String extra) {
        try (Connection conn = showControl.conn(); PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO link_channel(link_id, status, extra) VALUES(?, ?, ?);"
        )) {
            stmt.setInt(1, link_id);
            stmt.setInt(2, status);
            stmt.setString(3, extra);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            showControl.log("Something went wrong.");
        }
        return false;
    }

    public boolean enableLinkChannel(int link_id,int status) {
        try (Connection conn = showControl.conn(); PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO link_channel(link_id, status, extra) VALUES(?, ?, '');"
        )) {
            stmt.setInt(1, link_id);
            stmt.setInt(2, status);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            showControl.log("Something went wrong.");
        }
        return false;
    }

    public boolean disableLinkChannel(int link_id) {
        try (Connection conn = showControl.conn(); PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM link_channel WHERE link_id='?';"
        )) {
            stmt.setInt(1, link_id);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            showControl.log("Something went wrong.");
        }
        return false;
    }

    public boolean getLinkChannel(int link_id) { //UNFINISHED
        try (Connection conn = showControl.conn(); PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM link_channel WHERE link_id='?';"
        )) {
            stmt.setInt(1, link_id);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            showControl.log("Something went wrong.");
        }
        return false;
    }
}
