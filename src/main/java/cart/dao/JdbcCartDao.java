package cart.dao;

import cart.entity.Cart;
import cart.entity.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcCartDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcCartDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("cart_table")
                .usingGeneratedKeyColumns("id");
    }


    public void addProduct(Long userId,Long productId) {
        final MapSqlParameterSource parameterMap = new MapSqlParameterSource()
                .addValue("user_id",userId)
                .addValue("product_id", productId);
        simpleJdbcInsert.execute(parameterMap);
    }
    public List<Cart> findByUserEmail(final String email){
        final String sql = "SELECT *" +
                " FROM products_table" +
                " INNER JOIN cart_table ON products_table.id = cart_table.product_id" +
                " INNER JOIN user_table ON cart_table.user_id = user_table.id" +
                " WHERE user_table.user_email = ?";;
        return jdbcTemplate.query(sql,(rs, rowNum) ->new Cart(new Product(rs.getLong("id"),
                rs.getString("product_name"),
                rs.getInt("product_price"),
                rs.getString("product_image")),rs.getLong("cart_table.id")),
                email
        );
    }
    public void delete(Long id){
        final String sql = "DELETE FROM cart_table WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
