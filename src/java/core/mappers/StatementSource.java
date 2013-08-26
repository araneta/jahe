/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.mappers;

/**
 *
 * @author aldo
 */
public interface StatementSource {
    String sql();
    Object[] parameters();
}
