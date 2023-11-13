package com.zerobase.reserve.domain.entity;

import com.zerobase.reserve.domain.converter.RoleConverter;
import com.zerobase.reserve.domain.type.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @Convert(converter = RoleConverter.class)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Store> stores = new ArrayList<>();

    public void addStore(Store store) {
        stores.add(store);
        store.setMember(this);
    }

    public void removeStore(Store store) {
        stores.remove(store);
        store.setMember(null);
    }


}
