#!/usr/bin/perl

use lib q(build);
use Build::MDD;

my $build = Build::MDD->new(
    module_name         => 'MDD',
    dist_version_from   => 'bin/mdd.pl',
    dist_abstract       => 'MythDroid Daemon',
    dist_author         => 'foobum@gmail.com',
    build_requires      => { 'File::Copy' => 0, 'Test::More' => 0 },
    requires            => { 
                             'Sys::Hostname' => 0, 'Time::HiRes' => 0, 
                             'HTTP::Daemon'  => 0, 'URI::Escape' => 0,
                             'Image::Imlib2' => 0
                           },
    installdirs         => 'vendor',
    install_path        => { conf => '/etc' },
    auto_features       => {
        xosd_support        => {
            description         => 'Alternative XOSD-based OSD',
            requires            => { 'X::Osd' => 0 }
        },
        crypt               => {
            description         => 'CMux authentication and encryption',
            requires            => { 'Crypt::Rijndael' => 0 },
        }
    }
);

my $backend = ! $build->y_n(
    "Does this system serve as a frontend [yn]?"
);

$build->feature('backend' => $backend);

unless ($backend) {
    if (!$build->feature('xosd_support')) { 
        print "\nX::Osd was not found" . 
              " - the alternative XOSD-based OSD will not be available\n\n"; 
    } 
} 
 
if (!$build->feature('crypt')) { 
    print "\nCrypt::Rijndael was not found" . 
          " - connections to CMux will be unauthenticated and insecure\n\n"; 
} 

$build->create_build_script;
