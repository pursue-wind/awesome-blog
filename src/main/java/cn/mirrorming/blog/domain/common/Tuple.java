package cn.mirrorming.blog.domain.common;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Mireal
 */
@Data
@AllArgsConstructor(staticName = "of")
public class Tuple<A, B> {
    public final A first;
    public final B second;
}